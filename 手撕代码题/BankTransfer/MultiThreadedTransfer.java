package BankTransfer;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// �˻���
class Account {
    private String accountId;
    private double balance;
    private final Lock lock = new ReentrantLock();

    public Account(String accountId, double initialBalance) {
        this.accountId = accountId;
        this.balance = initialBalance;
    }

    public String getAccountId() {
        return accountId;
    }

    public double getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    // ��ȫ���������
    public void deposit(double amount) {
        lock.lock();
        try {
            balance += amount;
        } finally {
            lock.unlock();
        }
    }

    // ��ȫ�ؼ����������Ƿ�ɹ�
    public boolean withdraw(double amount) {
        lock.lock();
        try {
            if (balance >= amount) {
                balance -= amount;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public Lock getLock() {
        return lock;
    }
}

// ת�˷�����
class TransferService {
    // ��ȫת�˷�����ʹ����˳���������
    public static void transfer(Account from, Account to, double amount) throws InterruptedException {
        Account first = from;
        Account second = to;

        // ͨ���˻�ID�Ĺ�ϣֵȷ������˳�򣬱�������
        if (from.getAccountId().hashCode() > to.getAccountId().hashCode()) {
            first = to;
            second = from;
        }

        // ��ȡ����˳��̶�������ѭ���ȴ�
        first.getLock().lock();
        try {
            second.getLock().lock();
            try {
                // ִ��ת�˲���
                if (from.withdraw(amount)) {
                    to.deposit(amount);
                    System.out.printf("ת�˳ɹ����� %s �� %s����%.2f%n",
                            from.getAccountId(), to.getAccountId(), amount);
                } else {
                    System.out.printf("ת��ʧ�ܣ��˻� %s ����%n", from.getAccountId());
                }
            } finally {
                second.getLock().unlock();
            }
        } finally {
            first.getLock().unlock();
        }
    }
}

// ת�������࣬ʵ��Runnable�ӿ�
class TransferTask implements Runnable {
    private Account from;
    private Account to;
    private double amount;

    public TransferTask(Account from, Account to, double amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    @Override
    public void run() {
        try {
            TransferService.transfer(from, to, amount);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("ת�������жϣ�" + e.getMessage());
        }
    }
}

// ���࣬��ʾ���߳�ת��
public class MultiThreadedTransfer {
    public static void main(String[] args) throws InterruptedException {
        // �����˻�
        Account account1 = new Account("A1001", 1000.0);
        Account account2 = new Account("A1002", 2000.0);
        Account account3 = new Account("A1003", 1500.0);

        // �����̳߳�
        int threadCount = 5;
        Thread[] threads = new Thread[threadCount];

        // �������������ת���߳�
        threads[0] = new Thread(new TransferTask(account1, account2, 500.0));
        threads[1] = new Thread(new TransferTask(account2, account3, 300.0));
        threads[2] = new Thread(new TransferTask(account3, account1, 200.0));
        threads[3] = new Thread(new TransferTask(account2, account1, 700.0));
        threads[4] = new Thread(new TransferTask(account1, account3, 400.0));

        // ���������߳�
        for (Thread thread : threads) {
            thread.start();
        }

        // �ȴ������߳����
        for (Thread thread : threads) {
            thread.join();
        }

        // ��ӡ�������
        System.out.println("\n�����˻���");
        System.out.printf("�˻� %s: %.2f%n", account1.getAccountId(), account1.getBalance());
        System.out.printf("�˻� %s: %.2f%n", account2.getAccountId(), account2.getBalance());
        System.out.printf("�˻� %s: %.2f%n", account3.getAccountId(), account3.getBalance());
    }
}
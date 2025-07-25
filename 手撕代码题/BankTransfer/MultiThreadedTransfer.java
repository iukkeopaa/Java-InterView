package BankTransfer;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// 账户类
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

    // 安全地增加余额
    public void deposit(double amount) {
        lock.lock();
        try {
            balance += amount;
        } finally {
            lock.unlock();
        }
    }

    // 安全地减少余额，返回是否成功
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

// 转账服务类
class TransferService {
    // 安全转账方法：使用锁顺序避免死锁
    public static void transfer(Account from, Account to, double amount) throws InterruptedException {
        Account first = from;
        Account second = to;

        // 通过账户ID的哈希值确定锁的顺序，避免死锁
        if (from.getAccountId().hashCode() > to.getAccountId().hashCode()) {
            first = to;
            second = from;
        }

        // 获取锁的顺序固定，避免循环等待
        first.getLock().lock();
        try {
            second.getLock().lock();
            try {
                // 执行转账操作
                if (from.withdraw(amount)) {
                    to.deposit(amount);
                    System.out.printf("转账成功：从 %s 到 %s，金额：%.2f%n",
                            from.getAccountId(), to.getAccountId(), amount);
                } else {
                    System.out.printf("转账失败：账户 %s 余额不足%n", from.getAccountId());
                }
            } finally {
                second.getLock().unlock();
            }
        } finally {
            first.getLock().unlock();
        }
    }
}

// 转账任务类，实现Runnable接口
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
            System.err.println("转账任务被中断：" + e.getMessage());
        }
    }
}

// 主类，演示多线程转账
public class MultiThreadedTransfer {
    public static void main(String[] args) throws InterruptedException {
        // 创建账户
        Account account1 = new Account("A1001", 1000.0);
        Account account2 = new Account("A1002", 2000.0);
        Account account3 = new Account("A1003", 1500.0);

        // 创建线程池
        int threadCount = 5;
        Thread[] threads = new Thread[threadCount];

        // 创建并启动多个转账线程
        threads[0] = new Thread(new TransferTask(account1, account2, 500.0));
        threads[1] = new Thread(new TransferTask(account2, account3, 300.0));
        threads[2] = new Thread(new TransferTask(account3, account1, 200.0));
        threads[3] = new Thread(new TransferTask(account2, account1, 700.0));
        threads[4] = new Thread(new TransferTask(account1, account3, 400.0));

        // 启动所有线程
        for (Thread thread : threads) {
            thread.start();
        }

        // 等待所有线程完成
        for (Thread thread : threads) {
            thread.join();
        }

        // 打印最终余额
        System.out.println("\n最终账户余额：");
        System.out.printf("账户 %s: %.2f%n", account1.getAccountId(), account1.getBalance());
        System.out.printf("账户 %s: %.2f%n", account2.getAccountId(), account2.getBalance());
        System.out.printf("账户 %s: %.2f%n", account3.getAccountId(), account3.getBalance());
    }
}
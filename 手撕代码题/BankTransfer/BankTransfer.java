package BankTransfer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.StampedLock;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/21 16:43
 */
public class BankTransfer {

    class BankAccount {
        private final int id;
        private int balance;

        public BankAccount(int id, int balance) {
            this.id = id;
            this.balance = balance;
        }

        public void transferTo(BankAccount dest, int amount) {
            // 按ID排序加锁（避免死锁）
            if (this.id < dest.id) {
                synchronized (this) {
                    synchronized (dest) {
                        executeTransfer(dest, amount);
                    }
                }
            } else {
                synchronized (dest) {
                    synchronized (this) {
                        executeTransfer(dest, amount);
                    }
                }
            }
        }

        private void executeTransfer(BankAccount dest, int amount) {
            if (this.balance >= amount) {
                this.balance -= amount;
                dest.balance += amount;
                System.out.printf("Transferred %d from %d to %d%n", amount, this.id, dest.id);
            } else {
                System.out.printf("Insufficient funds in account %d%n", this.id);
            }
        }

        public int getBalance() {
            return balance;
        }
    }

//    class Bank {
//        private final Lock lock = new ReentrantLock();
//        private final Map<Integer, Account> accounts = new HashMap<>();
//
//        public void transfer(int fromId, int toId, int amount) {
//            lock.lock();
//            try {
//                Account from = accounts.get(fromId);
//                Account to = accounts.get(toId);
//                if (from != null && to != null && from.getBalance() >= amount) {
//                    from.debit(amount);
//                    to.credit(amount);
//                    System.out.printf("Transferred %d from %d to %d%n", amount, fromId, toId);
//                } else {
//                    System.out.println("Transfer failed");
//                }
//            } finally {
//                lock.unlock();
//            }
//        }
//
//        static class Account {
//            private int balance;
//            public Account(int initialBalance) { this.balance = initialBalance; }
//            public void debit(int amount) { balance -= amount; }
//            public void credit(int amount) { balance += amount; }
//            public int getBalance() { return balance; }
//        }
//    }
//
//    class BankAccount {
//        private final int id;
//        private int balance;
//        private final StampedLock lock = new StampedLock();
//
//        public BankAccount(int id, int balance) {
//            this.id = id;
//            this.balance = balance;
//        }
//
//        public void transferTo(BankAccount dest, int amount) {
//            // 获取两个账户的写锁
//            long stamp1 = this.lock.writeLock();
//            long stamp2 = dest.lock.writeLock();
//            try {
//                if (this.balance >= amount) {
//                    this.balance -= amount;
//                    dest.balance += amount;
//                    System.out.printf("Transferred %d from %d to %d%n", amount, this.id, dest.id);
//                } else {
//                    System.out.printf("Insufficient funds in %d%n", this.id);
//                }
//            } finally {
//                // 释放写锁（顺序无关）
//                dest.lock.unlockWrite(stamp2);
//                this.lock.unlockWrite(stamp1);
//            }
//        }
//
//        public int getBalance() {
//            // 乐观读（无锁）
//            long stamp = lock.tryOptimisticRead();
//            int balance = this.balance;
//            if (!lock.validate(stamp)) {
//                // 读过程中发生写操作，升级为悲观读锁
//                stamp = lock.readLock();
//                try {
//                    balance = this.balance;
//                } finally {
//                    lock.unlockRead(stamp);
//                }
//            }
//            return balance;
//        }
//    }
//
//
//    class BankAccount {
//        private final int id;
//        private int balance;
//        private final ReentrantLock lock = new ReentrantLock();
//
//        public BankAccount(int id, int balance) {
//            this.id = id;
//            this.balance = balance;
//        }
//
//        public void transferTo(BankAccount dest, int amount) throws InterruptedException {
//            // 按ID排序获取锁
//            boolean firstLocked = false;
//            boolean secondLocked = false;
//
//            try {
//                if (this.id < dest.id) {
//                    this.lock.lockInterruptibly();
//                    firstLocked = true;
//                    dest.lock.lockInterruptibly();
//                    secondLocked = true;
//                } else {
//                    dest.lock.lockInterruptibly();
//                    firstLocked = true;
//                    this.lock.lockInterruptibly();
//                    secondLocked = true;
//                }
//
//                if (this.balance >= amount) {
//                    this.balance -= amount;
//                    dest.balance += amount;
//                    System.out.printf("Transferred %d from %d to %d%n", amount, this.id, dest.id);
//                } else {
//                    System.out.printf("Insufficient funds in %d%n", this.id);
//                }
//            } finally {
//                // 按相反顺序释放锁
//                if (secondLocked) dest.lock.unlock();
//                if (firstLocked) this.lock.unlock();
//            }
//        }
//
//        public int getBalance() {
//            lock.lock();
//            try {
//                return balance;
//            } finally {
//                lock.unlock();
//            }
//        }
//    }
//
//    class BankAccount {
//        private final int id;
//        private final AtomicInteger balance;
//
//        public BankAccount(int id, int initialBalance) {
//            this.id = id;
//            this.balance = new AtomicInteger(initialBalance);
//        }
//
//        public void transferTo(BankAccount dest, int amount) {
//            // 尝试CAS操作完成转账（非阻塞）
//            int fromBalance = balance.get();
//            while (fromBalance >= amount) {
//                if (balance.compareAndSet(fromBalance, fromBalance - amount)) {
//                    // 转出成功，尝试转入
//                    dest.deposit(amount);
//                    System.out.printf("Transferred %d from %d to %d%n", amount, this.id, dest.id);
//                    return;
//                }
//                fromBalance = balance.get();
//            }
//            System.out.printf("Insufficient funds in %d%n", this.id);
//        }
//
//        private void deposit(int amount) {
//            // 使用CAS保证原子性
//            int current;
//            do {
//                current = balance.get();
//            } while (!balance.compareAndSet(current, current + amount));
//        }
//
//        public int getBalance() {
//            return balance.get();
//        }
//    }
}

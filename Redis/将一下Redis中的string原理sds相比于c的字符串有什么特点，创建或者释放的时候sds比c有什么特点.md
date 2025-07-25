### 1. **SDS 的核心特点**

#### 1.1 **二进制安全**

- **C 字符串**：以空字符`'\0'`结尾，因此无法存储包含`'\0'`的二进制数据（如图片、序列化对象）。
- **SDS**：通过显式记录字符串长度（`len`字段）来标识字符串结束，允许存储任意二进制数据，且可以包含多个`'\0'`。

#### 1.2 **O (1) 时间复杂度获取长度**

- **C 字符串**：需遍历整个字符串（`strlen()`），时间复杂度为 O (n)。
- **SDS**：直接读取`len`字段，时间复杂度为 O (1)。

#### 1.3 **预分配与惰性释放机制**

- **预分配**：当字符串增长时，SDS 会预分配额外空间（如翻倍），减少内存重新分配次数。
- **惰性释放**：当字符串缩短时，SDS 不会立即释放空间，而是记录`free`字段供后续使用，避免频繁内存操作。

#### 1.4 **避免缓冲区溢出**

- **C 字符串**：`strcat()`等操作需手动确保目标缓冲区足够大，否则可能溢出。
- **SDS**：API 会自动检查并扩展空间，保证操作安全。

### 2. **创建与释放的差异**

#### 2.1 **创建时的差异**

| **特性**       | **C 字符串**                        | **SDS**                              |
| -------------- | ----------------------------------- | ------------------------------------ |
| **内存分配**   | 需手动计算并分配（如`malloc(n+1)`） | 自动分配`len+free+1`字节，包含预分配 |
| **初始化开销** | 简单赋值，开销小                    | 需要初始化结构体（`len`、`free`等）  |
| **二进制安全** | 不支持，需额外处理                  | 天然支持，无需特殊处理               |
| **适用场景**   | 简单文本处理                        | 复杂场景（二进制数据、动态增长）     |

#### 2.2 **释放时的差异**

| **特性**     | **C 字符串**               | **SDS**                                |
| ------------ | -------------------------- | -------------------------------------- |
| **内存释放** | 直接`free()`，无需额外操作 | 需释放整个结构体（可能包含预分配空间） |
| **惰性释放** | 不支持，内存立即回收       | 支持，缩短时仅更新`free`字段           |
| **内存碎片** | 频繁分配 / 释放易产生碎片  | 预分配减少碎片（尤其适合频繁修改场景） |

### 3. **典型操作对比**

| **操作**   | **C 字符串**                     | **SDS**                         |
| ---------- | -------------------------------- | ------------------------------- |
| 获取长度   | `strlen()`，O(n)                 | 直接读取`len`，O(1)             |
| 字符串追加 | `strcat()`，需手动扩容，可能溢出 | `sdscat()`，自动扩容，安全高效  |
| 字符串缩短 | 手动截断，可能造成内存泄漏       | `sdstrim()`，惰性释放，内存复用 |
| 二进制安全 | 不支持                           | 支持（存储任意二进制数据）      |

### 4. **总结**

| **维度**     | **C 字符串**         | **SDS**                              |
| ------------ | -------------------- | ------------------------------------ |
| **设计目标** | 简单文本处理         | 高性能、安全的动态字符串             |
| **核心优势** | 轻量、简单           | 二进制安全、O (1) 长度、预分配       |
| **适用场景** | 静态字符串、简单操作 | Redis 内部字符串、需要频繁修改的场景 |
| **内存管理** | 手动控制，易出错     | 自动管理，减少分配 / 释放次数        |

Redis 选择 SDS 而非 C 字符串的根本原因在于：**Redis 作为高性能内存数据库，需要频繁操作字符串（如缓存、计数器），SDS 的设计通过预分配、惰性释放和二进制安全等特性，显著提升了字符串操作的效率和安全性**。
private Long expectedInsertions = 20000L;



private Double falseProbability = 0.01D;


1. 布隆过滤器可能存在假阳性，但是不存在假阴性
2. 因为位数组中的每一个可能被几元素所共享，所以不支持删除，要支持删除的话可以使用计数布隆过滤器和布谷鸟布隆过滤器。一个是用更多的位数（一般4位）来代替原来的单个位数，但是空间复杂度更高，第二个依赖与布谷鸟哈希函数


## 用户服务中 使用 

name: user-register-bloom-filter
expectedInsertions: 1000
falseProbability: 0.01
前端加解密 js文件链接：https://static.longfa.cloud/files/HoneyBadgerEncrypt.js 后期完善再上传到npm
支持前端RSA AES SM4 加解密

*js文件需要引入外部依赖 都写有备注 控制台cv执行就完工*

演示：

有参构造器 、也提供set方法 配置密钥

```vue
import {AesHandler, RsaHandler, SM4Handler} from "@/util/HoneyBadgerEncrypt"; //选择性导入

const key = "xxxxxxxxx"; //16 32 64 字节
const iv = "xxxx"; //16 字节

//公钥
const PUBLIC_KEY = 'xxxxxxx'
//私钥
const PRIVATE_KEY = 'xxxxxxxx'

const rsaHandler = new RsaHandler(PUBLIC_KEY,PRIVATE_KEY); //初始化RSA加密处理实例
const aesHandler = new AesHandler(key,iv) //初始化AES加密处理实例
const sm4Handler = new SM4Handler('c4a98b23a8d94035','2404308462934f9f')//初始化SM4加密处理实例
```

使用：

```js
let encryptRSA = sm4Handler.encrypt('123456');//明文
console.log(encryptRSA,'密文');
let decryptRSA = sm4Handler.decrypt('abcd'); //密文
console.log(decryptRSA,'明文')
```


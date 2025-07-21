package org.github.wqz;

public class lc468 {

    class Solution {
        public String validIPAddress(String queryIP) {
            // 检查是否可能是IPv4（含.）
            if (queryIP.contains(".")) {
                return isValidIPv4(queryIP) ? "IPv4" : "Neither";
            }
            // 检查是否可能是IPv6（含:）
            else if (queryIP.contains(":")) {
                return isValidIPv6(queryIP) ? "IPv6" : "Neither";
            }
            // 既不含.也不含:，无效
            else {
                return "Neither";
            }
        }

        // 验证是否为有效的IPv4地址
        private boolean isValidIPv4(String ip) {
            // 按.分割，必须得到4个部分
            String[] parts = ip.split("\\.");
            if (parts.length != 4) {
                return false;
            }
            for (String part : parts) {
                // 部分为空或长度>3 → 无效
                if (part.isEmpty() || part.length() > 3) {
                    return false;
                }
                // 长度>1且以0开头 → 前置零无效
                if (part.length() > 1 && part.charAt(0) == '0') {
                    return false;
                }
                // 检查是否全为数字
                for (char c : part.toCharArray()) {
                    if (!Character.isDigit(c)) {
                        return false;
                    }
                }
                // 检查数值是否≤255
                int num;
                try {
                    num = Integer.parseInt(part);
                } catch (NumberFormatException e) {
                    return false;
                }
                if (num > 255) {
                    return false;
                }
            }
            return true;
        }

        // 验证是否为有效的IPv6地址
        private boolean isValidIPv6(String ip) {
            // 按:分割，必须得到8个部分
            String[] parts = ip.split(":");
            if (parts.length != 8) {
                return false;
            }
            // 十六进制允许的字符
            String hexChars = "0123456789abcdefABCDEF";
            for (String part : parts) {
                // 部分为空或长度>4 → 无效
                if (part.isEmpty() || part.length() > 4) {
                    return false;
                }
                // 检查每个字符是否为合法十六进制字符
                for (char c : part.toCharArray()) {
                    if (hexChars.indexOf(c) == -1) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}

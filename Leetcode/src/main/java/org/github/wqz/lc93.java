package org.github.wqz;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: wjh
 * @Date: 2025/7/20 17:25
 */
public class lc93 {


    class Solution {
        public List<String> restoreIpAddresses(String s) {
            List<String> result = new ArrayList<>();
            if (s == null || s.length() < 4 || s.length() > 12) {
                return result;
            }
            backtrack(s, 0, new ArrayList<>(), result);
            return result;
        }

        private void backtrack(String s, int start, List<String> current, List<String> result) {
            // 若已找到4段且用完所有字符，加入结果
            if (current.size() == 4) {
                if (start == s.length()) {
                    result.add(String.join(".", current));
                }
                return;
            }

            // 尝试所有可能的分段长度（1-3）
            for (int len = 1; len <= 3 && start + len <= s.length(); len++) {
                String segment = s.substring(start, start + len);
                if (isValid(segment)) {
                    current.add(segment);
                    backtrack(s, start + len, current, result);
                    current.remove(current.size() - 1); // 回溯
                }
            }
        }

        private boolean isValid(String segment) {
            // 长度检查
            if (segment.length() == 0 || segment.length() > 3) {
                return false;
            }
            // 前导零检查（长度>1时不能以0开头）
            if (segment.length() > 1 && segment.charAt(0) == '0') {
                return false;
            }
            // 数值范围检查
            int num = Integer.parseInt(segment);
            return num >= 0 && num <= 255;
        }
    }
}

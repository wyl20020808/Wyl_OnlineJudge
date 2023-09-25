const keywords = [
    'auto', 'break', 'case', 'char', 'const', 'continue', 'default', 'do', 'double', 'else', 'enum', 'extern', 'float', 'for', 'goto', 'if', 'inline', 'int', 'long', 'register', 'restrict', 'return', 'short', 'signed', 'sizeof', 'static', 'struct', 'switch', 'typedef', 'union', 'unsigned', 'void', 'volatile', 'while', '_Bool', '_Complex', '_Imaginary',
    '#include<assert.h>', '#include<ctype.h>', '#include<errno.h>', '#include<float.h>', '#include<iso646.h>', '#include<limits.h>', '#include<locale.h>', '#include<math.h>', '#include<setjmp.h>', '#include<signal.h>', '#include<stdarg.h>', '#include<stddef.h>', '#include<stdio.h>', '#include<stdlib.h>', '#include<string.h>', '#include<time.h>',
    '#include<complex.h>', '#include<fenv.h>', '#include<inttypes.h>', '#include<stdalign.h>', '#include<stdbool.h>', '#include<stdint.h>', '#include<tgmath.h>', '#include<wchar.h>', '#include<wctype.h>',
    '#include<algorithm>', '#include<bitset>', '#include<complex>', '#include<deque>', '#include<exception>', '#include<fstream>', '#include<functional>', '#include<iomanip>', '#include<ios>', '#include<iosfwd>', '#include<iostream>', '#include<istream>', '#include<iterator>', '#include<limits>', '#include<list>', '#include<locale>', '#include<map>', '#include<memory>', '#include<new>', '#include<numeric>', '#include<ostream>', '#include<queue>', '#include<set>', '#include<sstream>', '#include<stack>', '#include<stdexcept>', '#include<streambuf>', '#include<string>', '#include<typeinfo>', '#include<utility>', '#include<valarray>', '#include<vector>',
    '#include<array>', '#include<atomic>', '#include<chrono>', '#include<condition_variable>', '#include<forward_list>', '#include<future>', '#include<initializer_list>', '#include<mutex>', '#include<random>', '#include<ratio>', '#include<regex>', '#include<scoped_allocator>', '#include<system_error>', '#include<thread>', '#include<tuple>', '#include<typeindex>', '#include<type_traits>', '#include<unordered_map>', '#include<unordered_set>',
    '#include<cstring>','using','namespace','std','main','cin','cout','scanf','printf',
    'getline','cin.getline','puts','getchar','%d','%lf','%s','%f',
    '#include<cstdio>','#include<cmath>'
];
export function getMatchingItems(word, words) {
    if (word === '') return [];
    if(words.length > 0) words = words.slice(0, -1);
    // console.log(words)
    // 使用正则表达式匹配所有的连续的字母、数字和下划线
    const regex = /\b\w+\b/g;
    const matchesInWords = words.match(regex);

    // 创建一个新的数组来存储 keywords 和 matchesInWords
    let combinedWords = [...keywords];
    if (matchesInWords) {
        combinedWords = [...combinedWords, ...matchesInWords];
    }

    // 使用 Set 对象去重
    const uniqueWords = [...new Set(combinedWords)];

    // 过滤出包含输入词的候选项，并计算相似度
    const matches = uniqueWords
        .filter(item => item.includes(word))
        .map(item => ({
            item,
            similarity: (word.length / item.length) - (item.indexOf(word) / item.length)
        }));

    // 按相似度排序，相似度高的在前
    matches.sort((a, b) => b.similarity - a.similarity);

    // 返回排序后的候选项
    return matches.map(match => match.item);
}
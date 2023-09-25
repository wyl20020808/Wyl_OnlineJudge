let algorithmsAndDataStructures = [
    // 数据结构
    "数组",
    "链表",
    "栈",
    "队列",
    "哈希表",
    "堆",
    "二叉树",
    "二叉搜索树",
    "平衡二叉树（如AVL树）",
    "红黑树",
    "B树和B+树",
    "线段树",
    "树状数组",
    "并查集",
    "字典树",
    "后缀树",
    "图（邻接矩阵、邻接表）",
    "稀疏表",
    "跳表",
    "布隆过滤器",
  
    // 算法
    "深度优先搜索",
    "广度优先搜索",
    "二分搜索",
    "快速排序",
    "归并排序",
    "堆排序",
    "动态规划",
    "贪心算法",
    "回溯算法",
    "分治算法",
    "最短路径（Dijkstra、Floyd、Bellman-Ford）",
    "最小生成树（Prim、Kruskal）",
    "拓扑排序",
    "并查集算法",
    "KMP字符串匹配",
    "Rabin-Karp字符串匹配",
    "Z-algorithm字符串匹配",
    "Manacher's Algorithm",
    "Tarjan强连通分量",
    "Ford-Fulkerson最大流",
    "Edmonds-Karp最大流",
    "Dinic's Algorithm",
    "Hungarian Algorithm（匈牙利算法）",
    "数论算法（如欧几里得算法、中国剩余定理）",
    "几何算法（如凸包、线段交点）",
    "NP问题的近似算法",
    "随机化算法",
    "概率算法",
    "线性规划",
    "网络流",
    "最大公约数和最小公倍数",
    "素数筛法",
    "快速幂",
    "高精度计算",
    "FFT（快速傅里叶变换）",
    "N皇后问题",
    "旅行商问题",
    "图着色问题",
  ];
  const languages = [
    { value: 45, label: "Assembly (NASM 2.14.02)", is_archived: false },
    { value: 46, label: "Bash (5.0.0)", is_archived: false },
    { value: 47, label: "Basic (FBC 1.07.1)", is_archived: false },
    { value: 75, label: "C (Clang 7.0.1)", is_archived: false },
    { value: 76, label: "C++ (Clang 7.0.1)", is_archived: false },
    { value: 48, label: "C (GCC 7.4.0)", is_archived: false },
    { value: 52, label: "C++ (GCC 7.4.0)", is_archived: false },
    { value: 49, label: "C (GCC 8.3.0)", is_archived: false },
    { value: 53, label: "C++ (GCC 8.3.0)", is_archived: false },
    { value: 50, label: "C (GCC 9.2.0)", is_archived: false },
    { value: 54, label: "C++ (GCC 9.2.0)", is_archived: false },
    { value: 86, label: "Clojure (1.10.1)", is_archived: false },
    { value: 51, label: "C# (Mono 6.6.0.161)", is_archived: false },
    { value: 77, label: "COBOL (GnuCOBOL 2.2)", is_archived: false },
    { value: 55, label: "Common Lisp (SBCL 2.0.0)", is_archived: false },

    { value: 56, label: "D (DMD 2.089.1)", is_archived: false },

    { value: 57, label: "Elixir (1.9.4)", is_archived: false },

    { value: 58, label: "Erlang (OTP 22.2)", is_archived: false },
    { value: 44, label: "Executable", is_archived: false },
    { value: 87, label: "F# (.NET Core SDK 3.1.202)", is_archived: false },
    { value: 59, label: "Fortran (GFortran 9.2.0)", is_archived: false },
    { value: 60, label: "Go (1.13.5)", is_archived: false },

    { value: 88, label: "Groovy (3.0.3)", is_archived: false },

    { value: 61, label: "Haskell (GHC 8.8.1)", is_archived: false },

    { value: 62, label: "Java (OpenJDK 13.0.1)", is_archived: false },

    { value: 63, label: "JavaScript (Node.js 12.14.0)", is_archived: false },
    { value: 78, label: "Kotlin (1.3.70)", is_archived: false },
    { value: 64, label: "Lua (5.3.5)", is_archived: false },
    { value: 89, label: "Multi-file program", is_archived: false },
    { value: 79, label: "Objective-C (Clang 7.0.1)", is_archived: false },

    { value: 65, label: "OCaml (4.09.0)", is_archived: false },

    { value: 66, label: "Octave (5.1.0)", is_archived: false },
    { value: 67, label: "Pascal (FPC 3.0.4)", is_archived: false },
    { value: 85, label: "Perl (5.28.1)", is_archived: false },
    { value: 68, label: "PHP (7.4.1)", is_archived: false },
    { value: 43, label: "Plain Text", is_archived: false },
    { value: 69, label: "Prolog (GNU Prolog 1.4.5)", is_archived: false },

    { value: 70, label: "Python (2.7.17)", is_archived: false },

    { value: 71, label: "Python (3.8.1)", is_archived: false },
    { value: 80, label: "R (4.0.0)", is_archived: false },

    { value: 72, label: "Ruby (2.7.0)", is_archived: false },

    { value: 73, label: "Rust (1.40.0)", is_archived: false },
    { value: 81, label: "Scala (2.13.2)", is_archived: false },
    { value: 82, label: "SQL (SQLite 3.27.2)", is_archived: false },
    { value: 83, label: "Swift (5.2.3)", is_archived: false },
    { value: 74, label: "TypeScript (3.7.4)", is_archived: false },
    {
      value: 84,
      label: "Visual Basic.Net (vbnc 0.0.0.5943)",
      is_archived: false,
    },
  ];
  let algorithmsAndDataStructuresOptions = algorithmsAndDataStructures.map(
    (item) => {
      return {
        value: item,
        label: item,
      };
    }
  );
  
  module.exports = {
    algorithmsAndDataStructures,
    algorithmsAndDataStructuresOptions,
    languages
  };
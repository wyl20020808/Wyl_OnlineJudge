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
    algorithmsAndDataStructuresOptions
  };
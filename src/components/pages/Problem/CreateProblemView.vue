<template>
  <div class="container">
    <h1 class="title">新建题目</h1>
    <el-form label-position="left" label-width="70px">
      <el-form-item label="题目名称">
        <el-input v-model="title" type="textarea" placeholder="请输入题目名称" class="input"></el-input>
      </el-form-item>
      <el-form-item label="题目背景">
        <el-input v-model="background" type="textarea" placeholder="请输入题目背景" class="input"></el-input>
      </el-form-item>
      <el-form-item label="题目描述">
        <el-input v-model="description" type="textarea" placeholder="请输入题目描述" class="input"></el-input>
      </el-form-item>
      <el-form-item label="输入格式">
        <el-input v-model="inputFormat" type="textarea" placeholder="请输入输入格式" class="input"></el-input>
      </el-form-item>
      <el-form-item label="输出格式">
        <el-input v-model="outputFormat" type="textarea" placeholder="请输入输出格式" class="input"></el-input>
      </el-form-item>
      <div v-for="(example, index) in examples" :key="index" class="example">
        <el-input v-model="example.input" placeholder="请输入样例输入" class="example-input"></el-input>
        <el-input v-model="example.output" placeholder="请输入样例输出" class="example-input"></el-input>
        <el-button type="danger"  @click="removeExample(index)" class="example-button">删除</el-button>
      </div>
      <el-button @click="addExample" class="add-button">添加样例</el-button>
      <el-form-item label="提示">
        <el-input v-model="hint" type="textarea" placeholder="请输入提示" class="input"></el-input>
      </el-form-item>
    </el-form>
    <el-button type="primary" @click="save" class="save-button">保存</el-button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      title: '',
      background: '',
      description: '',
      inputFormat: '',
      outputFormat: '',
      examples: [{ input: '', output: '' }],
      hint: ''
    };
  },
  methods: {
    addExample() {
      this.examples.push({ input: '', output: '' });
    },
    removeExample(index) {
      this.examples.splice(index, 1);
    },
    save() {
      // 在这里处理保存逻辑
      console.log(this.title, this.background, this.description, this.inputFormat, this.outputFormat, this.examples, this.hint);
    }
  }
};
</script>

<style>
.container {
  width: 100%;
  margin: 0 auto;
  padding: 20px;
  box-sizing: border-box;
}

.title {
  margin-bottom: 20px;
  font-size: 24px;
  font-weight: bold;
}

.input {
  width: 800px;
  min-height: 50px;
}

.example {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.example-input {
  width: 50%;
  min-height: 50px;
}

.example-button {
  width: 8%;
}

.add-button {
  margin-bottom: 20px;
}

.save-button {
  width: 50%;
}
</style>
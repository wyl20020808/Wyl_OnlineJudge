<template>
  <div :style="editorStyle" ref="editor">
    <FullscreenOutlined
      v-if="!isFullscreen"
      :style="iconStyle"
      @click="toggleFullscreen"
    />
    <FullscreenExitOutlined
      v-else
      :style="iconStyle"
      @click="toggleFullscreen"
    />
    <div
      v-if="showAutocomplete && items.length > 0"
      :style="{
        position: 'absolute',
        left: `${coords.left}px`,
        top: `${coords.top + 20}px`,
        zIndex: 1001,
      }"
    >
      <select
        v-model="selected"
        @change="onSelect"
        size="5"
        style="width: 200px"
      >
        <option v-for="item in items" :key="item" :value="item">
          {{ item }}
        </option>
      </select>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, computed, nextTick, onUnmounted, watch } from "vue";
import {
  FullscreenOutlined,
  FullscreenExitOutlined,
} from "@ant-design/icons-vue";
import { getMatchingItems } from "@/js/Editor/autoComplete";
import CodeMirror from "codemirror";
import "codemirror/lib/codemirror.css";
import "codemirror/mode/clike/clike.js";
import "codemirror/addon/display/fullscreen.js";
import "codemirror/addon/display/fullscreen.css";
import "codemirror/addon/hint/show-hint.js";
import "codemirror/addon/hint/show-hint.css";
import "codemirror/addon/hint/anyword-hint.js";
import "codemirror/addon/edit/closebrackets.js";
import "codemirror/addon/search/search.js";
import "codemirror/addon/dialog/dialog.js";
import "codemirror/addon/dialog/dialog.css";
import "codemirror/addon/search/searchcursor.js";
import "codemirror/addon/search/jump-to-line.js";
import "codemirror/addon/selection/active-line.js";

// 用户定义的变量或函数

const editor = ref(null);
let cmInstance = null;
const isFullscreen = ref(false);
let resizeObserver = null;

const showAutocomplete = ref(false);
const items = ref([]);
const selected = ref(null);
const coords = ref({ left: 0, top: 0 });

const select = ref(null);
let isSelecting = false;

onMounted(async () => {
  if (editor.value) {
    cmInstance = CodeMirror(editor.value, {
      lineNumbers: true,
      mode: "text/x-c++src",
      viewportMargin: Infinity,
      extraKeys: {
        Enter: function (cm) {
          if (showAutocomplete.value) {
            onSelect();
          } else {
            // 默认的回车键行为
            cm.execCommand("newlineAndIndent");
          }
        },
        Up: function (cm) {
          if (showAutocomplete.value) {
            // Move the selection up in the dropdown
            const index = items.value.indexOf(selected.value);
            if (index > 0) {
              cm.execCommand("goLineDown");
              selected.value = items.value[index - 1];
              // Prevent the default behavior
              return CodeMirror.Pass;
            }
          }
          // If showAutocomplete.value is false, let CodeMirror handle the up key event.
          else {
            cm.execCommand("goLineUp");
          }
        },
        Down: function (cm) {
          if (showAutocomplete.value) {
            // Move the selection down in the dropdown
            const index = items.value.indexOf(selected.value);
            if (index < items.value.length - 1) {
              selected.value = items.value[index + 1];
              // Prevent the default behavior
              cm.execCommand("goLineUp");
              return CodeMirror.Pass;
            }
          }
          // If showAutocomplete.value is false, let CodeMirror handle the down key event.
          else {
            cm.execCommand("goLineDown");
          }
        },
      },
      autoCloseBrackets: true,
      styleActiveLine: true,
    });

    // 确保 DOM 已经更新
    await nextTick(); // 更新全屏图标的位置
    updateIconPosition();

    // 创建一个新的 ResizeObserver 实例并开始监听编辑器的尺寸变化
    resizeObserver = new ResizeObserver(updateIconPosition);
    resizeObserver.observe(editor.value);

    cmInstance.on("change", () => {
      if (isSelecting) {
        isSelecting = false;
        return;
      }
      const cursor = cmInstance.getCursor();
      const token = cmInstance.getTokenAt(cursor);
      const currentWord = token.string;
      let allTextInEditor = cmInstance.getRange({line: 0, ch: 0}, cursor); 
      // Generate the list of matching items
      items.value = getMatchingItems(currentWord, allTextInEditor);
      // Get the cursor's coordinates
      coords.value = cmInstance.cursorCoords(cursor, "window");

      // Check if the current word fully matches all the autocomplete options
      // const isFullMatch = items.value.every((item) => item === currentWord);
      const isFullMatch = false;
      // Show the autocomplete list and select the first item
      if (items.value.length > 0 && !isFullMatch) {
        showAutocomplete.value = true;
        selected.value = items.value[0];
      } else {
        showAutocomplete.value = false;
      }
    });
  }
});

onUnmounted(() => {
  // 停止监听编辑器的尺寸变化
  if (resizeObserver) {
    resizeObserver.disconnect();
  }
});

const toggleFullscreen = () => {
  isFullscreen.value = !isFullscreen.value;
  updateIconPosition();
};

const iconStyle = ref({ position: "fixed", zIndex: 1000 });

const updateIconPosition = () => {
  const rect = editor.value.getBoundingClientRect();
  iconStyle.value.right = `${window.innerWidth - rect.right + 10}px`;
  iconStyle.value.bottom = `${window.innerHeight - rect.bottom + 10}px`;
};

const editorStyle = computed(() => {
  if (isFullscreen.value) {
    return {
      width: "50%",
      height: "100%",
      position: "fixed",
      right: 0,
      top: 0,
      zIndex: 1000,
    };
  } else {
    return {
      width: "1000px",
      height: "600px", // 你可以根据需要调整这个值
    };
  }
});

function onSelect() {
  const cursor = cmInstance.getCursor();
  const token = cmInstance.getTokenAt(cursor);

  // Replace the current word with the selected item
  cmInstance.replaceRange(
    selected.value,
    { line: cursor.line, ch: token.start },
    { line: cursor.line, ch: token.end }
  );
  isSelecting = true;
  showAutocomplete.value = false;
}
</script>

<style>
@import "codemirror/lib/codemirror.css";
@import "codemirror/addon/display/fullscreen.css";
@import "codemirror/addon/hint/show-hint.css";
@import "codemirror/addon/dialog/dialog.css";

.CodeMirror {
  font-family: "Consolas", monospace;
  height: 100%;
}

.CodeMirror-activeline-background {
  background: #e8f2ff;
}
</style>

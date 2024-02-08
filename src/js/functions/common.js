import { message } from 'ant-design-vue';
import { ElNotification } from 'element-plus'
export function warningMessage(messages) {
    message.warning(messages);
}

export function notice(context, noticeInfo) {
    ElNotification({
        title: noticeInfo.title,
        message: noticeInfo.message,
        type: noticeInfo.type,
    })
}
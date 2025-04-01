<template>
    <view class="content">ans-qs</view>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue'


// 获取token

onMounted(() => {
    get_token()
})

const get_token = () => {
    token.value = uni.getStorageSync(
        'Authorization'
    )

    if (!token.value) {
        uni.redirectTo({ url: "/pages/user/login" })
    }
}
const token = ref()




// 获取随机问题

enum qs_type {
    FILL_BLANK = 'FILL_BLANK',
    SINGLE_CHOICE = 'SINGLE_CHOICE',
    MULTIPLE_CHOICE = 'MULTIPLE_CHOICE',
    TRUE_FALSE = 'TRUE_FALSE'
}

enum qs_diff {
    EASY = 'EASY',
    MEDIUM = 'MEDIUM',
    HARD = 'HARD'
}

interface qs {
    id: number
    bank_sub: string
    type: string
    content: string
    answer: string
    options: Map<string, string> | null
    explanation: string
    difficulty: qs_diff
    creator: string
    create_time: Date
}

interface ans_type {
    correctAnswer: string
    options: Map<string, string> | null
}

interface qs_res {
    id: number
    bankId: number
    type: qs_type
    content: string
    answer: ans_type
    explanation: string
    difficulty: qs_diff
    creatorId: number
    createTime: Date
}


interface bank_type {
    id: number
    subject: string
}

const question = ref<qs>({
    id: 0,
    bank_sub: '',
    type: '单选',
    content: '',
    answer: '',
    options: null,
    explanation: '',
    difficulty: qs_diff.EASY,
    creator: '',
    create_time: new Date
})

const get_random_qs = (bank_id: number) => {
    uni.request({
        url: "/api/ans/" + bank_id,
        header: { 'Authorization': token.value }
    }).then((res: any) => {
        if (res.statusCode == 200) {
            const qs_res: qs_res = res.data
            question.value.id = qs_res.id
            uni.request({
                url: '/api/question/bank/' + qs_res.bankId,
                header: { 'Authorization': token.value }
            }).then((res: any) => {
                if (res.statusCode == 200) {
                    const bank_res: bank_type = res.data
                    question.value.bank_sub = bank_res.subject
                }
            })
            switch (qs_res.type) {
                case qs_type.SINGLE_CHOICE:
                    question.value.type = "单选"
                    break
                case qs_type.MULTIPLE_CHOICE:
                    question.value.type = "多选"
                    break
                case qs_type.FILL_BLANK:
                    question.value.type = "填空"
                    break
                case qs_type.TRUE_FALSE:
                    question.value.type = "判断"
                    break
            }
            question.value.content = qs_res.content
            question.value.answer = qs_res.answer.correctAnswer
            question.value.options = qs_res.answer.options
            question.value.explanation = qs_res.explanation
            question.value.difficulty = qs_res.difficulty
            uni.request({
                url: '/api/usr/name/' + qs_res.creatorId
            }).then((res: any) => {
                if (res.statusCode == 200) {
                    question.value.creator = res.data
                }
            })
            question.value.create_time = qs_res.createTime
        } else {
            uni.showToast({ title: res.data.toString() })
        }
    }).catch(err => uni.showToast({ title: err }))
}
</script>

<style lang="scss" scoped>
.content {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}
</style>
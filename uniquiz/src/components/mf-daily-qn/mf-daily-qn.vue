<template>
    <view class="content">
        <uni-section title="每日一题" type="line">
            <uni-card title="每日一题" :extra="question.bank_sub" thumbnail="/static/logo.png" @click="to_ans_qs">
                <text class="uni-body">
                    问题({{ question.type }})：{{ question.content }}<br>
                    <text v-if="question.options" v-for="(v, k) in question.options" :key="k">
                        {{ k }}: {{ v }}
                        <br>
                    </text>
                    难度：{{ question.difficulty }}
                </text>
            </uni-card>
        </uni-section>
    </view>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue'

// 跳转到答题页面

const to_ans_qs = () => {
    uni.navigateTo({ url: "/pages/quiz/ans-qs" })
}

// 获取token

onMounted(() => {
    get_token()
    get_random_qs()
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




// 获取问题

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

const get_random_qs = () => {
    uni.request({
        url: '/api/ans/daily',
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
    width: 100%;
    max-width: 100vw;
    /* Match the maximum width to the screen width */
    margin: 0 auto;
    /* Center the content */
    box-sizing: border-box;
    /* Ensure padding and border are included in the width */
}
</style>
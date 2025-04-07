<template>
    <view class="content">
        <uni-card title="每日一题" :extra="qn.sub" thumbnail="/static/logo.png">
            <view class="uni-body">
                问题({{ qn.type == qn_type.SINGLE_CHOICE ? '单选' :
                    qn?.type == qn_type.MULTIPLE_CHOICE ? '多选' :
                        qn.type == qn_type.FILL_BLANK ? '填空' : '判断'
                }})：{{ qn.content }}<br>
                <text v-if="qn.options.A" v-for="(v, k) in qn.options" :key="k">
                    {{ k }}: {{ v }}
                    <br>
                </text>
                <uni-forms :modelValue="qn">
                    <uni-forms-item v-if="opt_show" label="答案" name="answer">
                        <uni-data-checkbox multiple @change="change_ans" :localdata="ans" />
                    </uni-forms-item>
                    <uni-forms-item v-else-if="judge_show" label="答案" name="answer">
                        <uni-data-checkbox @change="select_ans" :localdata="select" />
                    </uni-forms-item>
                    <uni-forms-item v-else label="答案" name="answer">
                        <uni-easyinput type="text" v-model="qn.answer" />
                    </uni-forms-item>
                </uni-forms>
            </view>
        </uni-card>
    </view>
</template>

<script lang="ts" setup>
import { qn_diff, qn_type, type qn } from '@/stores/qn'
import { onLoad } from '@dcloudio/uni-app'
import { ref } from 'vue'

const opt_show = ref(true)
const judge_show = ref(false)


const change_ans = (t: any) => {
    console.log(t.detail.value)
    const arr = ref('')
    t.detail.value.forEach((a: string) => arr.value += a)
    console.log("arr:", arr.value)
    qn.value.answer = arr.value
    console.log("qn.ans:", qn.value.answer)
}

const select_ans = (t: any) => {
    qn.value.answer = t.detail.value
    console.log("qn.ans:", qn.value.answer)
}

const ans = ref([
    {
        text: 'A',
        value: 'A'
    },
    {
        text: 'B',
        value: 'B'
    },
    {
        text: 'C',
        value: 'C'
    },
    {
        text: 'D',
        value: 'D'
    },
])

const select = ref([
    {
        text: '正确',
        value: 'true'
    },
    {
        text: '错误',
        value: 'false'
    },
])



// 跳转到答题页面

onLoad((opt: any) => {
    qn.value = JSON.parse(decodeURIComponent(opt.qn))
})

const qn = ref<qn>({
    id: 0,
    sub: '',
    type: qn_type.SINGLE_CHOICE,
    content: '',
    options: {
        A: '',
        B: '',
        C: '',
        D: ''
    },
    answer: '',
    ana: '',
    diff: qn_diff.EASY,
    creator: '',
    create_time: new Date
})


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
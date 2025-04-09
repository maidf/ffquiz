<template>
    <view class="content" v-if="qn">
        <uni-card title="每日一题" :extra="qn.sub" thumbnail="/static/logo.png">
            <view class="uni-body">
                问题({{ qn.type == qn_type.SINGLE_CHOICE ? '单选' :
                    qn.type == qn_type.MULTIPLE_CHOICE ? '多选' :
                        qn.type == qn_type.FILL_BLANK ? '填空' : '判断'
                }})：{{ qn.content }}<br>
                <text v-if="qn.options.A" v-for="(v, k) in qn.options" :key="k">
                    {{ k }}: {{ v }}
                    <br>
                </text>
                <uni-forms v-if="!cor_ans">
                    <uni-forms-item v-if="opt_show" label="答案" name="answer">
                        <uni-data-checkbox multiple @change="change_ans" :localdata="ans" />
                    </uni-forms-item>
                    <uni-forms-item v-else-if="judge_show" label="答案" name="answer">
                        <uni-data-checkbox @change="select_ans" :localdata="select" />
                    </uni-forms-item>
                    <uni-forms-item v-else label="答案" name="answer">
                        <uni-easyinput type="text" v-model="usr_ans" />
                    </uni-forms-item>
                </uni-forms>
                <button v-if="!cor_ans" size="mini" @click="submit">提交</button>
            </view>
        </uni-card>

        <uni-card v-if="cor_ans" title="答案" :thumbnail="show_icon">
            <view class="uni-body">
                <text>正确答案：{{ cor_ans }} <br></text>
                <text>提交答案：{{ usr_ans }} <br></text>
                <text>题目解析：{{ qn.ana }} <br></text>
            </view>
        </uni-card>
    </view>
</template>

<script lang="ts" setup>
import { useAnsStore } from '@/stores/ans'
import { qn_diff, qn_type, type qn } from '@/stores/qn'
import { onLoad } from '@dcloudio/uni-app'
import { storeToRefs } from 'pinia'
import { ref, watch } from 'vue'

const show_icon = ref()
const opt_show = ref(true)
const judge_show = ref(false)


const change_ans = (t: any) => {
    console.log(t.detail.value)
    const arr = ref('')
    t.detail.value.forEach((a: string) => arr.value += a)
    console.log("arr:", arr.value)
    usr_ans.value = arr.value
    console.log("usr_ans.value:", usr_ans.value)
}

const select_ans = (t: any) => {
    usr_ans.value = t.detail.value
    console.log("usr_ans.value:", usr_ans.value)
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

const store = useAnsStore()
const { cor_ans } = storeToRefs(store)
const { start_ans, end_ans } = store

const usr_ans = ref<string>('')
const submit = () => {
    if (qn.value) {
        end_ans(usr_ans.value)
        if (usr_ans.value == cor_ans.value) {
            show_icon.value = "/static/check.png"
        } else {
            show_icon.value = "/static/close.png"
        }
    }

}

watch(cor_ans, (newAns) => {
    if (usr_ans.value == newAns) {
        show_icon.value = "/static/check.png"
    } else {
        show_icon.value = "/static/close.png"
    }
})

watch(qn, (newQn) => {
    if (newQn) {
        switch (newQn.type) {
            case qn_type.SINGLE_CHOICE:
                opt_show.value = true
                judge_show.value = false
                break
            case qn_type.MULTIPLE_CHOICE:
                opt_show.value = true
                judge_show.value = false
                break
            case qn_type.FILL_BLANK:
                opt_show.value = false
                judge_show.value = false
                break
            case qn_type.TRUE_FALSE:
                opt_show.value = false
                judge_show.value = true
                break
        }
        // 清空上一次的答案
        usr_ans.value = ''
        if (newQn.id > 0) {
            start_ans({
                qn_id: newQn.id,
                exam_id: null
            })
        }

    }
}, { immediate: true })
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
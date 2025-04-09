<template>
    <view v-if="qs">
        <uni-card v-for="(qn, index) in qs" :key="index" :title="'第' + (index + 1) + '题'" :extra="qn.sub"
            thumbnail="/static/logo.png">
            <view class="uni-body">
                问题({{ qn.type == qn_type.SINGLE_CHOICE ? '单选' :
                    qn?.type == qn_type.MULTIPLE_CHOICE ? '多选' :
                        qn.type == qn_type.FILL_BLANK ? '填空' : '判断'
                }})：{{ qn.content }}<br>
                <text v-if="qn.options.A" v-for="(v, k) in qn.options" :key="k">
                    {{ k }}: {{ v }}
                    <br>
                </text>
                <uni-forms>
                    <uni-forms-item v-if="qn.type == qn_type.SINGLE_CHOICE" label="答案" name="answer">
                        <uni-data-checkbox v-model="qn.answer" :localdata="ans" />
                    </uni-forms-item>
                    <uni-forms-item v-else-if="qn.type == qn_type.MULTIPLE_CHOICE" label="答案" name="answer">
                        <uni-data-checkbox multiple v-model="qn.answer" :localdata="ans" />
                    </uni-forms-item>
                    <uni-forms-item v-else-if="qn.type == qn_type.TRUE_FALSE" label="答案" name="answer">
                        <uni-data-checkbox v-model="qn.answer" :localdata="select" />
                    </uni-forms-item>
                    <uni-forms-item v-else label="答案" name="answer">
                        <uni-easyinput type="text" v-model="qn.answer" />
                    </uni-forms-item>
                </uni-forms>
            </view>
        </uni-card>
        <button @click="submit(qs)">提交</button>
    </view>
</template>

<script lang="ts" setup>
import { useExamStore } from '@/stores/exam'
import { type paper } from '@/stores/paper'
import { qn_type, type qn } from '@/stores/qn'
import { onLoad } from '@dcloudio/uni-app'
import { storeToRefs } from 'pinia'
import { ref, watch } from 'vue'

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



onLoad(async (opt: any) => {
    paper.value = await JSON.parse(decodeURIComponent(opt.paper))
    if (paper.value)
        req_qs(paper.value.id)
})

const paper = ref<paper>()

const store = useExamStore()
const { qs } = storeToRefs(store)
const { req_qs } = store

const submit = (qs: qn[]) => {

    const anses = qs.map(e => {
        console.log("e: ", e)
        const res = e.answer.toString().replace(/[^a-zA-Z]/g, '')
        console.log("res: ", res)

        return e.type === qn_type.MULTIPLE_CHOICE || e.type === qn_type.SINGLE_CHOICE ? {
            qn_id: e.id,
            usr_ans: e.answer.toString().replace(/[^a-zA-Z]/g, '')
        } : {
            qn_id: e.id,
            usr_ans: e.answer
        }
    })

    console.log("usr_ans: ", anses)
}

watch(qs, () => {
}, { immediate: true })

</script>

<style lang="scss" scoped></style>
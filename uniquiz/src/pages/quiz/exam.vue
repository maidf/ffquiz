<template>
    <view>
        <uni-nav-bar v-if="qs" :start="false" :fixed="true" shadow left-text="开始考试" right-text="提交" title="自定义导航栏"
            @clickLeft="start" @clickRight="submit(qs)">
            <view class="input-view">
                <uni-countdown @timeup="time_up(qs)" :show-day="false" :minute="limit"></uni-countdown>
            </view>
        </uni-nav-bar>
        <view v-if="show_qs && qs">
            <uni-card v-for="(qn, index) in qs" :key="index" :title="'第' + (index + 1) + '题'" :extra="qn.sub"
                thumbnail="/static/logo.png">
                <view class="uni-body">
                    问题({{ qn.type == qn_type.SINGLE_CHOICE ? '单选' :
                        qn.type == qn_type.MULTIPLE_CHOICE ? '多选' :
                            qn.type == qn_type.FILL_BLANK ? '填空' : '判断'
                    }})：{{ qn.content }}<br>
                    <text v-if="qn.options.A" v-for="(v, k) in qn.options" :key="k">
                        {{ k }}: {{ v }}
                        <br>
                    </text>
                    <uni-forms>
                        <uni-forms-item v-if="qn.type == qn_type.SINGLE_CHOICE" label="答案" name="answer">
                            <uni-data-checkbox v-model="qn.usr_ans" :localdata="ans" />
                        </uni-forms-item>
                        <uni-forms-item v-else-if="qn.type == qn_type.MULTIPLE_CHOICE" label="答案" name="answer">
                            <uni-data-checkbox multiple v-model="qn.usr_ans" :localdata="ans" />
                        </uni-forms-item>
                        <uni-forms-item v-else-if="qn.type == qn_type.TRUE_FALSE" label="答案" name="answer">
                            <uni-data-checkbox v-model="qn.usr_ans" :localdata="select" />
                        </uni-forms-item>
                        <uni-forms-item v-else label="答案" name="answer">
                            <uni-easyinput type="text" v-model="qn.usr_ans" />
                        </uni-forms-item>
                    </uni-forms>
                </view>
                <uni-collapse v-if="is_submit">
                    <uni-collapse-item title="答案解析" :show-animation="true">
                        <template v-slot:title>
                            <uni-list>
                                <uni-list-item title="答案解析" :show-extra-icon="true" :extra-icon="qn.answer == qn.usr_ans.toString().replace(/[^a-zA-Z]/g, '') ?
                                    {
                                        color: '#4cd964',
                                        size: '20',
                                        type: 'checkmarkempty'
                                    } :
                                    {
                                        color: 'red',
                                        size: '20',
                                        type: 'closeempty'
                                    }">
                                </uni-list-item>
                            </uni-list>
                        </template>
                        <text>正确答案：{{ qn.answer }} <br></text>
                        <text>提交答案：{{ qn.usr_ans.toString().replace(/[^a-zA-Z]/g, '') }} <br></text>
                        <text>题目解析：{{ qn.ana }}</text>
                    </uni-collapse-item>
                </uni-collapse>
            </uni-card>
        </view>
    </view>
</template>

<script lang="ts" setup>
import { useExamStore, type exam_qn } from '@/stores/exam'
import { type paper } from '@/stores/paper'
import { qn_type, type qn } from '@/stores/qn'
import { onLoad } from '@dcloudio/uni-app'
import { storeToRefs } from 'pinia'
import { ref, watch } from 'vue'

const is_submit = ref(false)
const limit = ref(0)
const show_qs = ref(false)

const time_up = (qs: exam_qn[]) => {
    if (show_qs.value && !is_submit.value) {
        submit(qs)
    }
}

const start = () => {
    if (paper.value) {
        limit.value = paper.value.time_limit
        show_qs.value = true
        const { start_exam } = useExamStore()
        start_exam({ qn_id: null, paper_id: paper.value.id })
    }

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



onLoad(async (opt: any) => {
    paper.value = await JSON.parse(decodeURIComponent(opt.paper))
    if (paper.value)
        req_qs(paper.value.id)
})

const paper = ref<paper>()

const store = useExamStore()
const { qs } = storeToRefs(store)
const { req_qs } = store

const submit = (qs: exam_qn[]) => {
    const anses = qs.map(e => {
        console.log("e: ", e)
        const res = e.usr_ans.toString().replace(/[^a-zA-Z]/g, '')
        console.log("res: ", res)
        return {
            qn_id: e.id,
            usr_ans: res
        }
    })
    console.log("显示答案解析")
    is_submit.value = true
    limit.value = 0
    console.log("重置倒计时: ", limit.value)
    const { end_exam } = useExamStore()
    end_exam(anses)
    console.log("usr_ans: ", anses)
}

watch(qs, () => {
}, { immediate: true })
</script>

<style lang="scss" scoped>
$nav-height: 30px;

.input-view {
    /* #ifndef APP-PLUS-NVUE */
    display: flex;
    /* #endif */
    flex-direction: row;
    // width: 500rpx;
    flex: 1;
    background-color: #f8f8f8;
    height: $nav-height;
    border-radius: 15px;
    padding: 0 15px;
    flex-wrap: nowrap;
    margin: 7px 0;
    line-height: $nav-height;
}
</style>
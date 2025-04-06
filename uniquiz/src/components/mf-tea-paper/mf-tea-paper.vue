<template>
    <view class="content">
        <uni-card title="试卷信息" thumbnail="/static/logo.png">
            <view class="uni-body">
                <uni-forms :modelValue="paper">
                    <uni-forms-item label="试卷名称" name="name">
                        <uni-easyinput type="text" v-model="paper.name" />
                    </uni-forms-item>
                    <uni-forms-item label="试卷难度" name="diff">
                        <uni-data-checkbox @change="change_diff" v-model="paper.diff" :localdata="diff" />
                    </uni-forms-item>
                    <uni-forms-item label="时间限制" name="time_limit">
                        <uni-easyinput type="text" v-model="paper.time_limit" />
                    </uni-forms-item>
                    <uni-forms-item label="试卷总分" name="total_score">
                        <uni-easyinput disabled :inputBorder="false" class="disable"
                            :styles="{ color: '#fff', disableColor: '#696969' }" type="text" v-model="paper.total_score" />
                    </uni-forms-item>
                    <uni-forms-item label="试卷作者" name="creator">
                        <uni-easyinput disabled :inputBorder="false" class="disable"
                            :styles="{ color: '#fff', disableColor: '#696969' }" type="text" v-model="paper.creator" />
                    </uni-forms-item>
                </uni-forms>
                <button size="mini" @click="">修改</button>
                <button size="mini" @click="">删除</button>
                <button size="mini" @click="show_qs">查看题目</button>
                <button size="mini" @click="show_qn">添加题目</button>
            </view>
        </uni-card>

        <mf-add-qn v-if="mf_qn" :paper_id="paper.id"></mf-add-qn>
        <mf-qs v-if="mf_qs" :paper_id="paper.id"></mf-qs>
    </view>
</template>

<script lang="ts" setup>
import { useBankStore } from '@/stores/bank'
import { onLoad } from '@dcloudio/uni-app'
import { ref } from 'vue'
import mfAddQn from './mf-add-qn/mf-add-qn.vue'
import mfQs from './mf-qs/mf-qs.vue'
import type { paper } from '@/stores/paper'
import { qn_diff } from '@/stores/qn'

const mf_qs = ref(false)

const show_qs = () => {
    mf_qn.value = false
    mf_qs.value = true
}

const mf_qn = ref(false)

const show_qn = () => {
    mf_qs.value = false
    mf_qn.value = true
}




const change_diff = (t: any) => {
    paper.value.diff = t.detail.value
    console.log("paper.diff:", paper.value.diff)
}

const diff = ref([
    {
        text: '简单',
        value: qn_diff.EASY
    },
    {
        text: '中等',
        value: qn_diff.MEDIUM
    },
    {
        text: '困难',
        value: qn_diff.HARD
    },
])



const paper = ref<paper>({
    id: 0,
    name: '',
    diff: qn_diff.EASY,
    time_limit: 0,
    total_score: 0,
    create_time: new Date,
    creator: ''
})

onLoad((opt: any) => {
    paper.value = JSON.parse(decodeURIComponent(opt.paper))
})

const { update_bank, delete_bank } = useBankStore()
</script>

<style lang="scss" scoped>
button {
    margin-right: 10px;
}

.disable {
    opacity: 1;
    border-radius: 2px;
}
</style>
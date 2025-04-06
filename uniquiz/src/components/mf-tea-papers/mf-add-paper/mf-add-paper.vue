<template>
    <view class="content">
        <uni-card title="创建试卷" thumbnail="/static/logo.png">
            <view class="uni-body">
                <uni-forms :modelValue="paper">
                    <uni-forms-item label="试卷名称" name="name">
                        <uni-easyinput type="text" v-model="paper.name" />
                    </uni-forms-item>
                    <uni-forms-item label="试卷难度" name="diff">
                        <uni-data-checkbox @change="change_diff" v-model="paper.diff" :localdata="diff" />
                    </uni-forms-item>
                    <uni-forms-item label="时间限制" name="time_limit">
                        <uni-easyinput class="time_limit" type="text" v-model="paper.time_limit" /> 分钟
                    </uni-forms-item>
                </uni-forms>
                <button size="mini" @click="add_paper(paper)">添加</button>
            </view>
        </uni-card>
    </view>
</template>

<script lang="ts" setup>
import { usePaperStore, type paper } from '@/stores/paper'
import { qn_diff } from '@/stores/qn'
import { ref } from 'vue'

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
    creator: '',
    create_time: new Date
})

const { add_paper } = usePaperStore()

</script>

<style lang="scss" scoped>
.time_limit {
    width: fit-content;
    display: inline-block;
}
</style>
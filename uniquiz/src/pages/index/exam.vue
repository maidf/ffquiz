<template>
    <view>
        <view v-if="records && records.length > 0">
            <uni-card v-for="(v, index) in show_records" :key="index" :title="v.name"
                :extra="v.diff === qn_diff.EASY ? '简单' : v.diff === qn_diff.MEDIUM ? '中等' : '困难'"
                @click="to_rd_qs(v.id)"
                thumbnail="/static/logo.png">
                <view class="uni-body">
                    <view>分数: {{ v.score }}/{{ v.total_score }} <br></view>
                    <view>时间: {{ v.time_limit }}分钟</view>
                    <view>开始时间：{{ v.start_time }}</view>
                    <view>结束时间：{{ v.end_time }}</view>
                    <uni-button type="warn" size="mini" @click="rm_exam(v.id)">删除</uni-button>
                </view>
            </uni-card>
        </view>
        <view v-else>
            <text>暂无考试记录</text>
        </view>

        <uni-pagination v-if="records && records.length > 0" :total="records.length" :current="now_page"
            :pageSize="one_page_num" :show-icon="true" @change="page_change" />
    </view>
</template>

<script lang="ts" setup>
import { useExamStore } from '@/stores/exam'
import { qn_diff, qn_type } from '@/stores/qn'
import { storeToRefs } from 'pinia'
import { computed, onMounted, ref } from 'vue'

const to_rd_qs = (v: number) => {
    uni.navigateTo({ url: "/pages/index/exam-ans?exam_id=" + v })
}


// 分页相关
const now_page = ref(1)
const one_page_num = 10

const store = useExamStore()
const { req_rds, delete_exam } = store
const { records } = storeToRefs(store)

// 当前页要显示的记录
const show_records = computed(() => {
    if (!records.value || records.value.length === 0) return []
    const start = (now_page.value - 1) * one_page_num
    const end = start + one_page_num
    return records.value.slice(start, end)
})

// 翻页处理
const page_change = (e: { current: number }) => {
    now_page.value = e.current
}

const rm_exam = async (exam_id: number) => {
    try {
        await delete_exam(exam_id)
        // 如果删除后当前页没有数据且不是第一页，则自动返回上一页
        if (show_records.value.length === 0 && now_page.value > 1) {
            now_page.value -= 1
        }
    } catch (error) {
        uni.showToast({ title: "删除失败" })
    }
}

onMounted(() => {
    req_rds()
})
</script>

<style lang="scss" scoped></style>
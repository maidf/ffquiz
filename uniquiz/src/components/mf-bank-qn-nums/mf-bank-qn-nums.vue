<!-- bank-chart-page.vue -->
<template>
    <view class="chart-page" v-if="bank_qn_nums">
        <!-- 题目数量分布 -->
        <view class="chart-box">
            <view class="chart-title">
                <uni-icons type="list" size="24" color="#409-eff" />
                <text class="title-text">题目数量</text>
            </view>
            <view class="charts-box">
                <qiun-data-charts type="pie" :chartData="qn_data" />
            </view>

        </view>

        <!-- 使用量排名 -->
        <view class="chart-box">
            <view class="chart-title">
                <uni-icons type="eye" size="24" color="#67-c23a" />
                <text class="title-text">题目使用量</text>
            </view>
            <view class="charts-box">
                <qiun-data-charts type="pie" :chartData="use_data" />
            </view>
        </view>

        <!-- 错题数量对比 -->
        <view class="chart-box">
            <view class="chart-title">
                <uni-icons type="fire" size="24" color="#f56-c6c" />
                <text class="title-text">错题数量</text>
            </view>
            <view class="charts-box">
                <qiun-data-charts type="pie" :chartData="err_data" />
            </view>
        </view>

        <!-- 错误率排行 -->
        <view class="chart-box">
            <view class="chart-title">
                <uni-icons type="flag" size="24" color="#e6-a23c" />
                <text class="title-text">错误率/%</text>
            </view>
            <view class="charts-box">
                <qiun-data-charts type="bar" :chartData="err_rate_data" />
            </view>
        </view>
    </view>
    <view v-else>
        暂时没有数据
    </view>
</template>

<script setup>
import { useStatStore } from '@/stores/stat'
import { storeToRefs } from 'pinia'
import { computed, onMounted } from 'vue'

const store = useStatStore()
const { bank_qn_nums } = storeToRefs(store)
const { get_bank_qn_nums } = store

const qn_data = computed(() => ({
    series: [{
        data: bank_qn_nums.value
            .sort((a, b) => b.qn_nums - a.qn_nums)
            .map(i => ({
                name: i.name,
                value: i.qn_nums,
            }))
    }]
}))

const use_data = computed(() => ({
    series: [{
        data: [...bank_qn_nums.value]
            .sort((a, b) => b.use_nums - a.use_nums)
            .map(item => ({
                name: item.name,
                value: item.use_nums,
            }))
    }]
}))

const err_data = computed(() => ({
    series: [{
        data: bank_qn_nums.value
            .sort((a, b) => b.err_nums - a.err_nums)
            .map(item => ({
                name: item.name,
                value: item.err_nums,
            }))
    }]
}))

const err_rate_data = computed(() => ({
    series: [
        {
            name: '错误率',
            data: bank_qn_nums.value
                .sort((a, b) => b.err_nums / b.use_nums - a.err_nums / a.use_nums)
                .map(item =>
                    Number((item.err_nums / item.use_nums * 100).toFixed(1)
                    ))
        }
    ],
    categories: bank_qn_nums.value
        .map(item => item.name)
}))

onMounted(() => {
    get_bank_qn_nums()
})
</script>

<style lang="scss">
.charts-box {
    width: 100%;
    height: 300px;
}
</style>
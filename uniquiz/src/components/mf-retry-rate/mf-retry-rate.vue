<template>
    <view class="container">
        <!-- 标题部分 -->
        <view class="header">
            <text class="title">题目重试率排行榜</text>
            <text class="subtitle">TOP 10 练习数据统计</text>
        </view>

        <!-- 数据卡片列表 -->
        <view v-if="retry_rate" class="card-list">
            <view v-for="item in retry_rate" :key="item.id" class="card">
                <!-- 内容主体 -->
                <view class="card-content">
                    <view class="meta">
                        <text class="content-text">📚 {{ item.content }}</text>
                        <text class="sub-text">📌 {{ item.sub }}</text>
                    </view>

                    <!-- 难度标签 -->
                    <view class="diff-tag" :style="{ backgroundColor: get_diff_color(item.diff) }">
                        {{ format_diff(item.diff) }}
                    </view>
                </view>

                <!-- 数据指标 -->
                <view class="stats">
                    <view class="stat-item">
                        <text class="stat-label">重试率</text>
                        <view class="progress-container">
                            <view class="progress-bar" :style="{ width: `${item.retry_rate}%` }" />
                            <text class="stat-value">{{ item.retry_rate }}%</text>
                        </view>
                    </view>

                    <view class="stat-item">
                        <text class="stat-label">平均准确率</text>
                        <view class="accuracy">
                            <text class="accuracy-value" :style="{ color: get_accuracy_color(item.avg_acc) }">
                                {{ item.avg_acc }}%
                            </text>
                            <view class="trend-icon">
                                📈
                            </view>
                        </view>
                    </view>
                </view>
            </view>
        </view>

        <!-- 加载状态 -->
        <view v-else class="loading">
            <text>加载数据中...</text>
        </view>
    </view>
</template>

<script setup lang="ts">
import { qn_diff } from '@/stores/qn'
import { useStatStore } from '@/stores/stat'
import { storeToRefs } from 'pinia'
import { onMounted } from 'vue'

const store = useStatStore()
const { retry_rate } = storeToRefs(store)
const { get_top_10_retry_rate_stat } = store

onMounted(() => {
    get_top_10_retry_rate_stat()
})

// 难度颜色映射
const get_diff_color = (diff: qn_diff) => {
    const colors = {
        [qn_diff.EASY]: '#4CAF50',    // 绿色
        [qn_diff.MEDIUM]: '#FF9800', // 橙色
        [qn_diff.HARD]: '#F44336'    // 红色
    }
    return colors[diff] || '#607D8B'
}

// 格式化难度显示
const format_diff = (diff:qn_diff) => {
    const diff_map = {
        [qn_diff.EASY]: '简单',
        [qn_diff.MEDIUM]: '中等',
        [qn_diff.HARD]: '困难'
    }
    return diff_map[diff] || '未知'
}

// 准确率颜色映射
const get_accuracy_color = (acc: number) => {
    return acc >= 80 ? '#4CAF50' : acc >= 60 ? '#FF9800' : '#F44336'
}
</script>

<style lang="scss" scoped>
.container {
    padding: 20rpx;
    background: #f5f7fa;
}

.header {
    padding: 30rpx;
    text-align: center;
}

.title {
    display: block;
    font-size: 36rpx;
    font-weight: 600;
    color: #2c3e50;
}

.subtitle {
    font-size: 28rpx;
    color: #7f8c8d;
}

.card-list {
    margin: 20rpx 0;
}

.card {
    background: white;
    border-radius: 16rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);

    &-content {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 30rpx;
    }
}

.meta {
    flex: 1;
}

.content-text {
    display: block;
    font-size: 32rpx;
    color: #34495e;
    margin-bottom: 10rpx;
}

.sub-text {
    font-size: 26rpx;
    color: #95a5a6;
}

.diff-tag {
    padding: 8rpx 20rpx;
    border-radius: 40rpx;
    color: white;
    font-size: 24rpx;
    font-weight: 500;
}

.stats {
    display: flex;
    gap: 30rpx;
}

.stat-item {
    flex: 1;
}

.stat-label {
    display: block;
    font-size: 26rpx;
    color: #7f8c8d;
    margin-bottom: 10rpx;
}

.progress-container {
    position: relative;
    height: 40rpx;
    background: #eee;
    border-radius: 20rpx;
    overflow: hidden;
}

.progress-bar {
    height: 100%;
    background: #3498db;
    transition: width 0.3s ease;
}

.stat-value {
    position: absolute;
    right: 15rpx;
    top: 50%;
    transform: translateY(-50%);
    color: white;
    font-size: 24rpx;
}

.accuracy {
    display: flex;
    align-items: center;
    gap: 10rpx;
}

.accuracy-value {
    font-size: 32rpx;
    font-weight: 500;
}

.trend-icon {
    font-size: 36rpx;
}

.loading {
    text-align: center;
    padding: 40rpx;
    color: #95a5a6;
}
</style>
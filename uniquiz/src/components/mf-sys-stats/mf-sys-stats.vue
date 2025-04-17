<template>
    <view v-if="sys_stats">
        <uni-card title="统计数据">
            <view>用户数量: {{ sys_stats.user }}</view>
            <view>题库数量: {{ sys_stats.bank }}</view>
            <view>题目数量: {{ sys_stats.qn }}</view>
            <view>试卷数量: {{ sys_stats.paper }}</view>
            <view>
                今天答题次数: {{ sys_stats.today_use }}
                <text class="updown" v-if="(sys_stats.today_use - sys_stats.yesday_use) > 0">
                    <uni-icons type="fire-filled" size="12" />
                    {{ 1 - sys_stats.today_use / sys_stats.yesday_use }}
                </text>
                <text class="updown" v-if="(sys_stats.today_use - sys_stats.yesday_use) < 0">
                    <uni-icons type="pulldown" size="12" />
                    {{ 1 - sys_stats.today_use / sys_stats.yesday_use }}
                </text>
            </view>
            <view>
                今天答题用户数量: {{ sys_stats.today_use_user }}
                <text class="updown" v-if="(sys_stats.today_use_user - sys_stats.yesday_use_user) > 0">
                    <uni-icons type="fire-filled" size="12" />
                    {{ 1 - sys_stats.today_use_user / sys_stats.yesday_use_user }}
                </text>
                <text class="updown" v-if="(sys_stats.today_use_user - sys_stats.yesday_use_user) < 0">
                    <uni-icons type="pulldown" size="12" />
                    {{ 1 - sys_stats.today_use_user / sys_stats.yesday_use_user }}
                </text>
            </view>
            <view>
                昨天答题次数: {{ sys_stats.yesday_use }}
            </view>
            <view>昨天答题用户数量: {{ sys_stats.yesday_use_user }}</view>
        </uni-card>
    </view>
    <view v-else>暂无数据</view>
</template>

<script lang="ts" setup>
import { useStatStore } from '@/stores/stat'
import { storeToRefs } from 'pinia'
import { onMounted } from 'vue'

const store = useStatStore()
const { sys_stats } = storeToRefs(store)
const { get_sys_stats } = store

onMounted(() => {
    get_sys_stats()
    console.log(sys_stats.value)
})
</script>

<style lang="scss" scoped>
.updown {
    color: red;
}
</style>
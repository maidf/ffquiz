<template>
    <view>
        <view v-if="records && records.length > 0">
            <uni-card v-for="(v, index) in show_records" :key="index" :title="v.sub"
                :extra="v.diff === qn_diff.EASY ? '简单' : v.diff === qn_diff.MEDIUM ? '中等' : '困难'"
                thumbnail="/static/logo.png">
                <view class="uni-body">
                    问题({{ v.type == qn_type.SINGLE_CHOICE ? '单选' :
                        v.type == qn_type.MULTIPLE_CHOICE ? '多选' :
                            v.type == qn_type.FILL_BLANK ? '填空' : '判断'
                    }})：{{ v.content }}<br>
                    <text v-if="v.options.A" v-for="(v1, k1) in v.options" :key="k1">
                        {{ k1 }}: {{ v1 }}
                        <br>
                    </text>
                    <br>
                    <view>题目解析：{{ v.ana }} <br></view>
                    <view>正确答案：{{ v.ans }}</view>
                    <view v-if="v.usr_ans === v.ans" style="color: orange;">提交答案：{{ v.usr_ans }}</view>
                    <view v-else style="color: red;">提交答案：{{ v.usr_ans }}</view>
                    <view>开始时间：{{ v.start_time }}</view>
                    <view>结束时间：{{ v.end_time }}</view>
                    <uni-button type="warn" size="mini" @click="remove_record(v.id)">删除</uni-button>
                </view>
            </uni-card>
        </view>
        <view v-else>
            <text>暂无答题记录</text>
        </view>

        <uni-pagination 
            v-if="records && records.length > 0"
            :total="records.length" 
            :current="now_page" 
            :pageSize="one_page_num"
            :show-icon="true" 
            @change="page_change" 
        />
    </view>
</template>

<script lang="ts" setup>
import { useAnsStore } from '@/stores/ans'
import { qn_diff, qn_type } from '@/stores/qn'
import { storeToRefs } from 'pinia'
import { computed, onMounted, ref } from 'vue'

// 分页相关
const now_page = ref(1)
const one_page_num = 10

const store = useAnsStore()
const { req_rds, delete_ans } = store
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

// 删除记录
const remove_record = async (id: number) => {
    try {
        await delete_ans(id)
        // 如果删除后当前页没有数据且不是第一页，则自动返回上一页
        if (show_records.value.length === 0 && now_page.value > 1) {
            now_page.value -= 1
        }
    } catch (error) {
        console.error('删除失败:', error)
    }
}

onMounted(() => {
    req_rds()
})
</script>

<style lang="scss" scoped>
</style>
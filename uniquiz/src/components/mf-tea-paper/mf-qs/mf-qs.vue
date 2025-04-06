<template>
    <uni-table border stripe emptyText="暂无更多数据">
        <!-- 表头行 -->
        <uni-tr>
            <uni-th>分类</uni-th>
            <uni-th>类型</uni-th>
            <uni-th>题目</uni-th>
            <uni-th>选项</uni-th>
            <uni-th>答案</uni-th>
            <uni-th>解析</uni-th>
            <uni-th>难度</uni-th>
            <uni-th>分值</uni-th>
            <uni-th align="center">操作</uni-th>
        </uni-tr>
        <!-- 表格数据行 -->
        <uni-tr v-for="v in qs" :key="v.id">
            <uni-td>{{ v.sub }}</uni-td>
            <uni-td>{{ v.type }}</uni-td>
            <uni-td>{{ v.content }}</uni-td>
            <uni-td v-if="v.options.A">
                <view v-for="(v1, k1) in v.options" :key="k1"> {{ k1 }} : {{ v1 }}</view>
            </uni-td>
            <uni-td v-else></uni-td>
            <uni-td>{{ v.answer }}</uni-td>
            <uni-td>{{ v.ana }}</uni-td>
            <uni-td>{{ v.diff }}</uni-td>
            <uni-td>{{ v.score }}</uni-td>
            <uni-td align="center">
                <uni-button @click="rm_qn(paper_id, v.id)" type="warn" size="mini">删除</uni-button>
            </uni-td>
        </uni-tr>
    </uni-table>

</template>

<script lang="ts" setup>
import { usePaperStore, type paper_qn } from '@/stores/paper'
import { storeToRefs } from 'pinia'
import { onMounted, ref } from 'vue'

onMounted(() => {
    req_qs(paper_id)
})

const { paper_id } = defineProps(['paper_id'])

const store = usePaperStore()

const { qs } = storeToRefs(store)
const { req_qs, rm_qn } = store
</script>

<style lang="scss" scoped>
uni-button {
    margin-right: 10px;
}
</style>
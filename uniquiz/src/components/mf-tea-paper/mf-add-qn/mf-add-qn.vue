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
        <uni-tr v-for="(v, k) in qs as paper_qn[]" :key="k">
            <uni-td>{{ v.sub }}</uni-td>
            <uni-td>{{ v.type }}</uni-td>
            <uni-td>{{ v.content }}</uni-td>
            <uni-td v-if="v.options.A">
                A: {{ v.options.A }} <br>
                B: {{ v.options.B }} <br>
                C: {{ v.options.C }} <br>
                D: {{ v.options.D }} <br>
            </uni-td>
            <uni-td v-else></uni-td>
            <uni-td>{{ v.answer }}</uni-td>
            <uni-td>{{ v.ana }}</uni-td>
            <uni-td>{{ v.diff }}</uni-td>
            <uni-td><uni-easyinput style="width: fit-content;" type="number" v-model="v.score" /></uni-td>
            <uni-td align="center">
                <uni-button @click="add_qn(paper_id, v.id, v.score)" size="mini">添加</uni-button>
            </uni-td>
        </uni-tr>
    </uni-table>

</template>

<script lang="ts" setup>
import { usePaperStore, type paper_qn } from '@/stores/paper'
import { useQnStore } from '@/stores/qn'
import { storeToRefs } from 'pinia'
import { onMounted, ref } from 'vue'

onMounted(() => {
    req_qs()
})

const { paper_id } = defineProps(['paper_id'])

const paper_store = usePaperStore()
const { add_qn } = paper_store

const qn_store = useQnStore()
const { req_qs } = qn_store

const { qs } = storeToRefs(qn_store)
</script>

<style lang="scss" scoped>
uni-button {
    margin-right: 10px;
}
</style>
<template>
    <uni-table border stripe emptyText="暂无更多数据">
        <!-- 表头行 -->
        <uni-tr>
            <uni-th>类型</uni-th>
            <uni-th>题目</uni-th>
            <uni-th>选项</uni-th>
            <uni-th>答案</uni-th>
            <uni-th>解析</uni-th>
            <uni-th>难度</uni-th>
            <uni-th align="center">操作</uni-th>
        </uni-tr>
        <!-- 表格数据行 -->
        <uni-tr v-for="(v, k) in qs" :key="k">
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
            <uni-td align="center">
                <uni-button @click="sel_upd_qn(v)" size="mini">编辑</uni-button>
                <uni-button @click="delete_qn(v.id)" type="warn" size="mini">删除</uni-button>
            </uni-td>
        </uni-tr>
    </uni-table>

</template>

<script lang="ts" setup>
import { useBankStore } from '@/stores/bank'
import { useQnStore, type qn } from '@/stores/qn'
import { storeToRefs } from 'pinia'
import { onMounted, ref } from 'vue'

onMounted(() => {
    req_qs(bank_id)
})

const sel_upd_qn = (qn: qn) => {
    old_qn.value = qn
    upd_state.value = true
}
const upd_state = defineModel<boolean>('upd_state', { default: false })
const old_qn = defineModel<qn>('old_qn')
const { bank_id } = defineProps(['bank_id'])

const bank_store = useBankStore()
const { qs } = storeToRefs(bank_store)
const { req_qs } = bank_store

const qn_store = useQnStore()
const { delete_qn } = qn_store

</script>

<style lang="scss" scoped>
uni-button {
    margin-right: 10px;
}
</style>
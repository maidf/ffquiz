<template>
    <uni-grid :column="5" :showBorder="false" :square="false">
        <uni-grid-item>
        </uni-grid-item>

        <uni-grid-item @click="to_retry_rate">
            <uni-icons type="loop" size="normal" color="#777" />
            <uni-tag text="重刷排行" type="warning" style="text-align: center;" :circle="true"></uni-tag>
        </uni-grid-item>
        <uni-grid-item @click="to_bank_qn_nums">
            <uni-icons type="folder-add-filled" size="normal" color="#777" />
            <uni-tag text="题目数量" type="primary" style="text-align: center;" :circle="true"></uni-tag>
        </uni-grid-item>
        <uni-grid-item @click="to_sys_stats">
            <uni-icons type="staff-filled" size="normal" color="#777" />
            <uni-tag text="网站统计" type="success" style="text-align: center;" :circle="true"></uni-tag>
        </uni-grid-item>

        <uni-grid-item>
        </uni-grid-item>
    </uni-grid>

</template>

<script lang="ts" setup>
const props = defineProps()

const to_retry_rate = () => {
    console.log("点击按钮")
    uni.navigateTo({ url: "/pages/stats/retry-rate" })
}

const to_bank_qn_nums = () => {
    console.log("点击按钮")
    uni.navigateTo({ url: "/pages/stats/bank-qn-nums" })
}

const to_sys_stats = () => {
    console.log("点击按钮")
    uni.navigateTo({ url: "/pages/stats/sys-stats" })
}
</script>

<style lang="scss" scoped></style>
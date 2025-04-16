<template>
    <uni-grid :column="5" :showBorder="false" :square="false">
        <uni-grid-item>
        </uni-grid-item>
        
        <uni-grid-item @click="click">
            <uni-icons type="loop" :size="30" color="#777" />
            <uni-tag text="重刷排行" type="warning" style="text-align: center;" :circle="true"></uni-tag>
        </uni-grid-item>
        <uni-grid-item>
            <uni-icons type="folder-add-filled" :size="30" color="#777" />
            <uni-tag text="题目数量" type="primary" style="text-align: center;" :circle="true"></uni-tag>
        </uni-grid-item>
        <uni-grid-item>
            <uni-icons type="staff-filled" :size="30" color="#777" />
            <uni-tag text="用户数量" type="success" style="text-align: center;" :circle="true"></uni-tag>
        </uni-grid-item>

        <uni-grid-item>
        </uni-grid-item>
    </uni-grid>

</template>

<script lang="ts" setup>
const props = defineProps()

const click = () => {
    console.log("点击按钮")
    uni.navigateTo({ url: "/pages/stats/retry-rate" })
}
</script>

<style lang="scss" scoped></style>
<template>
    <view class="content">
        <uni-section title="题库" type="line">
            <mf-add-bank></mf-add-bank>
            <mf-bank></mf-bank>
        </uni-section>
    </view>
</template>

<script lang="ts" setup>
import mfBank from "./mf-bank/mf-bank.vue"
import mfAddBank from "./mf-add-bank/mf-add-bank.vue"
</script>

<style lang="scss" scoped>
.content {
    width: 100%;
    max-width: 100vw;
    /* Match the maximum width to the screen width */
    margin: 0 auto;
    /* Center the content */
    box-sizing: border-box;
    /* Ensure padding and border are included in the width */
}
</style>
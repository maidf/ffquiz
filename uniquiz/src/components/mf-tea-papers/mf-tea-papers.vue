<template>
    <view class="content">
        <uni-section title="试卷" type="line">
            <mf-add-paper></mf-add-paper>
            <mf-paper></mf-paper>
        </uni-section>
    </view>
</template>

<script lang="ts" setup>
import mfPaper from "./mf-paper/mf-paper.vue"
import mfAddPaper from "./mf-add-paper/mf-add-paper.vue"
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
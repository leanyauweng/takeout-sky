<template>
    <div class="mobile-frame">
      <div class="content-area">
  开发中
      </div>
      
    </div>
  </template>
  
  <script setup>
  </script>
  
  
  <style scoped>
  .mobile-frame {
    width: 375px;
    height: 812px;
    border: 10px solid #000;
    border-radius: 30px;
    margin: 20px auto;
    overflow: hidden;
    background: #fff;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.2);
    display: flex;
    flex-direction: column;
    position: relative;
  }
  
  .content-area {
    flex: 1;
    overflow-y: auto;
    padding-bottom: 60px;
  }
  
  </style>
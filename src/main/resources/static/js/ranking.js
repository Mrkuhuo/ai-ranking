document.addEventListener('DOMContentLoaded', () => {
  const downloadImageLink = document.getElementById('download-image-link');

  downloadImageLink.addEventListener('click', async function(event) {
    event.preventDefault(); // 阻止默认行为，防止页面跳转

    // 获取要转换为图片的表格元素
    const tableElement = document.querySelector('#ranking-table:not(.no-data-alert)');

    // 检查是否有数据表格存在
    if (tableElement) {
      try {
        // 使用html2canvas将表格渲染为Canvas
        const canvas = await html2canvas(tableElement, {
          useCORS: true, // 如果表格中有跨域图片资源，请启用此项
          allowTaint: true, // 如果有跨域图片且不支持CORS，可尝试启用此选项
          scale: window.devicePixelRatio || 1, // 考虑高清屏缩放比例
        });

        // 将Canvas转换为DataURL（image/png格式）
        const imageData = canvas.toDataURL('image/png');

        // 创建一个隐藏的可下载链接
        const downloadLink = document.createElement('a');
        downloadLink.href = imageData;
        downloadLink.download = 'table_data.png'; // 设置下载的文件名
        downloadLink.style.display = 'none';

        // 插入到DOM中
        document.body.appendChild(downloadLink);

        // 触发点击下载
        if (downloadLink.click) {
          downloadLink.click();
        } else if (document.createEvent) { // 处理兼容性问题
          const evt = document.createEvent('MouseEvents');
          evt.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
          downloadLink.dispatchEvent(evt);
        }

        // 下载完成后移除隐藏链接
        setTimeout(() => {
          document.body.removeChild(downloadLink);
        }, 100); // 等待一下再移除，确保浏览器已经开始下载
      } catch (error) {
        console.error('表格转图片失败：', error);
      }
    } else {
      alert('暂无数据可供下载！');
    }
  });
});

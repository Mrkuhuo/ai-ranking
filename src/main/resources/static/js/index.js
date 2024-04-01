// 修改 sendMessage 函数，处理自定义类型的选择
function sendMessage() {
    const keyword = $('#myInput').val();
    let selectedTagId = '';
    let displayCount = $('#displayCountSelect').val();
    let customTagName = $('#customTagName').val().trim();
    console.log(customTagName);

    // 检查是否有自定义类型或已选择的标签
    const selectedTag = $('.tag-item.selected');
    if (selectedTag.length > 0) {
        // 如果选择了普通标签，则忽略自定义类型
        selectedTagId = selectedTag.attr('data-id');
    } else if (customTagName) {
        // 如果没有选择普通标签但有自定义类型，则使用自定义类型
        selectedTagId = 'custom:' + customTagName;
    } else {
        alert('请选择一个领域或输入自定义类型后再提交！');
        return;
    }

    const apiUrl = 'keyword';

    if (!keyword) {
        alert('请输入关键词后再提交！');
        return;
    }

    // 将关键词、标签ID和展示条数封装到一个对象中发送给后端
    const dataToSend = { keyword, tagId: selectedTagId, displayCount };

    $("#bt").prop('disabled', true);
    $("#bt").text("生成中");
    $.ajax({
        type: 'POST',
        url: apiUrl,
        data: JSON.stringify(dataToSend),
        contentType: 'application/json',
        success: function(responseData, textStatus, jqXHR) {
            if (jqXHR.status === 200) {
                window.location.href = 'ranking';
            } else {
                alert('提交过程中发生错误，请稍后重试。');
                $("#bt").prop('disabled', false);
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.error('Error submitting keyword:', errorThrown);
            alert('提交过程中发生错误，请稍后重试。');
            $("#bt").prop('disabled', false);
        }
    });
}

// 修改 selectTag 函数，取消已有标签的选择
function selectTag(tagElement) {
    const customInputWrapper = document.getElementById('customInputWrapper');
    const customTagButton = document.getElementById('customTagButton');

    if (tagElement.classList.contains('custom-tag')) {
        // 如果点击的是自定义标签，则显示自定义输入框并清除已选标签
        if (previouslySelected) {
            previouslySelected.classList.remove('selected');
        }
        customInputWrapper.style.display = 'block';
        document.getElementById('customTagName').focus();
    } else {
        // 如果点击的是普通标签，则选择该标签并隐藏自定义输入框，同时显示自定义标签
        const previouslySelected = document.querySelector('.tag-item.selected');
        if (previouslySelected) {
            previouslySelected.classList.remove('selected');
        }
        tagElement.classList.add('selected');
        customInputWrapper.style.display = 'none';
        // 显示自定义标签
        customTagButton.style.display = 'inline-block';
    }
}



document.addEventListener("DOMContentLoaded", function () {
    setDefaultHighlight();
});



function setDefaultHighlight() {
    const firstTagItem = document.querySelector('.tag-item');
    selectTag(firstTagItem);
}

function toggleCustomInput() {
    const customInputWrapper = document.getElementById('customInputWrapper');
    const customTagButton = document.getElementById('customTagButton');
    const allTags = document.querySelectorAll('.tag-item');

    if (customInputWrapper.style.display === 'none') {
        // 清除所有普通标签的高亮效果
        allTags.forEach((tag) => {
            tag.classList.remove('selected');
        });

        // 显示自定义输入框并清除已选普通标签的高亮
        customInputWrapper.style.display = 'block';
        document.getElementById('customTagName').focus();

        // 隐藏自定义标签
        customTagButton.style.display = 'none';

        // 将自定义标签视作被点击的标签（此处实际上不需要，因为已经清除了所有标签的高亮）
        // selectTag(customTagButton);
    } else {
        customInputWrapper.style.display = 'none';
        // 当自定义输入框关闭时，需要恢复自定义标签的显示
        customTagButton.style.display = 'inline-block';
    }
}

// 修改自定义标签点击事件处理函数
document.getElementById('customTagButton').addEventListener('click', function () {
    toggleCustomInput();
});

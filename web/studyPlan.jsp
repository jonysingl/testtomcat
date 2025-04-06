<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>学习计划 - 智能聊天助手</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="css/studyPlan.css">
</head>
<body>
    <a href="chatbot.jsp" class="back-button">
        <i class="fas fa-arrow-left"></i>
        返回聊天
    </a>

    <div class="container">
        <!-- 左侧：任务状态 -->
        <div class="plan-section">
            <div class="section-header">
                <h2>任务状态</h2>
                <div class="current-day-display" id="currentDayDisplay">第1天</div>
            </div>
            <div class="task-status-list">
                <div class="task-group">
                    <h3 class="task-group-title">
                        <i class="fas fa-clock"></i>
                        未完成任务
                    </h3>
                    <div class="task-list" id="leftPendingList">
                        <!-- 未完成任务将通过JavaScript动态生成 -->
                    </div>
                </div>
                <div class="task-group">
                    <h3 class="task-group-title">
                        <i class="fas fa-check-circle"></i>
                        已完成任务
                    </h3>
                    <div class="task-list" id="leftCompletedList">
                        <!-- 已完成任务将通过JavaScript动态生成 -->
                    </div>
                </div>
            </div>
        </div>

        <!-- 中间：30天学习计划 -->
        <div class="plan-section">
            <div class="section-header">
                <h2>30天学习计划</h2>
            </div>
            <div class="snake-layout" id="timelineContainer">
                <!-- 时间线将通过JavaScript动态生成 -->
            </div>
        </div>

        <!-- 右侧：我的计划 -->
        <div class="plan-section">
            <div class="section-header">
                <h2>我的计划</h2>
                <button class="add-plan-button" onclick="showAddPlanPopup()">
                    <i class="fas fa-plus"></i>
                    添加计划
                </button>
            </div>
            <div class="my-plan-list" id="myPlanList">
                <div class="my-plan-item">
                    <h3 class="my-plan-title">Java基础学习</h3>
                    <p class="my-plan-description">掌握Java核心概念和基础语法，包括数据类型、控制流程、面向对象等基础知识</p>
                </div>

                <div class="my-plan-item">
                    <h3 class="my-plan-title">Spring框架学习</h3>
                    <p class="my-plan-description">学习Spring Boot和Spring MVC框架，掌握依赖注入、AOP等核心特性</p>
                </div>

                <div class="my-plan-item">
                    <h3 class="my-plan-title">数据库基础</h3>
                    <p class="my-plan-description">学习MySQL数据库基础知识，掌握SQL语句编写和数据库设计</p>
                </div>

                <div class="my-plan-item">
                    <h3 class="my-plan-title">前端技术学习</h3>
                    <p class="my-plan-description">学习HTML、CSS和JavaScript基础，掌握前端页面开发技能</p>
                </div>
            </div>
        </div>
    </div>

    <!-- 任务弹出框 -->
    <div class="popup-overlay" id="popupOverlay"></div>
    <div class="task-popup" id="taskPopup">
        <div class="task-popup-header">
            <h3 class="task-popup-title" id="popupTitle">第1天任务</h3>
            <button class="close-popup" onclick="closePopup()">
                <i class="fas fa-times"></i>
            </button>
        </div>
        <div class="task-popup-content">
            <div class="popup-task-list" id="popupTaskList">
                <!-- 任务列表将通过JavaScript动态生成 -->
            </div>
        </div>
    </div>

    <script>
        // 初始化当前选中的天数
        let currentDay = 1;

        // 模拟每天的任务数据
        const dailyTasks = {
            1: [
                { id: 1, text: "Java环境配置", completed: false },
                { id: 2, text: "Hello World程序", completed: false },
                { id: 3, text: "基本数据类型", completed: false }
            ],
            2: [
                { id: 4, text: "运算符和表达式", completed: false },
                { id: 5, text: "流程控制语句", completed: false }
            ],
            3: [
                { id: 6, text: "类和对象基础", completed: false },
                { id: 7, text: "继承和多态", completed: false }
            ],
            4: [
                { id: 8, text: "接口和抽象类", completed: false },
                { id: 9, text: "异常处理", completed: false }
            ],
            5: [
                { id: 10, text: "集合框架", completed: false },
                { id: 11, text: "泛型编程", completed: false }
            ]
        };

        // 生成时间线
        function generateTimeline() {
            const container = document.getElementById('timelineContainer');
            const days = 30;
            const cols = 7;
            const rows = Math.ceil(days / cols);
            
            container.innerHTML = '';
            
            for (let row = 0; row < rows; row++) {
                const rowDiv = document.createElement('div');
                rowDiv.className = 'timeline-row';
                
                const rowStartDay = row * cols + 1;
                const rowItems = [];
                
                for (let col = 0; col < cols; col++) {
                    let currentDay;
                    if (row % 2 === 0) {
                        currentDay = rowStartDay + col;
                    } else {
                        currentDay = rowStartDay + (cols - 1 - col);
                    }
                    
                    if (currentDay > days) break;
                    
                    const timelineItem = document.createElement('div');
                    timelineItem.className = 'timeline-item';
                    
                    // 添加水平连接线
                    if (currentDay < days) {
                        const horizontalConnector = document.createElement('div');
                        if (row % 2 === 0) {
                            // 偶数行，从左到右
                            if (col < cols - 1) {
                                horizontalConnector.className = 'timeline-connector horizontal right';
                                timelineItem.appendChild(horizontalConnector);
                            }
                        } else {
                            // 奇数行，从右到左
                            if (col > 0) {
                                horizontalConnector.className = 'timeline-connector horizontal left';
                                timelineItem.appendChild(horizontalConnector);
                            }
                        }
                        
                        // 添加垂直连接线
                        if (row < rows - 1) {
                            if ((row % 2 === 0 && col === cols - 1) || (row % 2 === 1 && col === 0)) {
                                const verticalConnector = document.createElement('div');
                                verticalConnector.className = 'timeline-connector vertical';
                                timelineItem.appendChild(verticalConnector);
                            }
                        }
                    }
                    
                    const dot = document.createElement('div');
                    dot.className = 'timeline-dot';
                    dot.setAttribute('data-day', currentDay);
                    dot.addEventListener('click', function() {
                        showTaskPopup(currentDay);
                        updateDayDisplay(currentDay);
                    });
                    
                    const content = document.createElement('div');
                    content.className = 'timeline-content';
                    
                    const dayLabel = document.createElement('div');
                    dayLabel.className = 'timeline-day';
                    dayLabel.textContent = '第' + currentDay + '天';
                    
                    timelineItem.appendChild(dot);
                    content.appendChild(dayLabel);
                    timelineItem.appendChild(content);
                    
                    if (row % 2 === 0) {
                        rowItems.push(timelineItem);
                    } else {
                        rowItems.unshift(timelineItem);
                    }
                }
                
                rowItems.forEach(item => rowDiv.appendChild(item));
                container.appendChild(rowDiv);
            }
        }

        // 显示任务弹出框
        function showTaskPopup(day) {
            const popup = document.getElementById('taskPopup');
            const overlay = document.getElementById('popupOverlay');
            const title = document.getElementById('popupTitle');
            const taskList = document.getElementById('popupTaskList');
            
            currentDay = day;
            title.textContent = '第' + day + '天任务';
            
            taskList.innerHTML = '';
            const tasks = dailyTasks[day] || [];
            
            if (tasks.length === 0) {
                taskList.innerHTML = '<div class="popup-task-item">暂无任务</div>';
            } else {
                tasks.forEach(task => {
                    const taskItem = document.createElement('div');
                    taskItem.className = 'popup-task-item';
                    taskItem.innerHTML = 
                        '<input type="checkbox" class="task-checkbox" ' +
                        'id="task' + task.id + '" ' +
                        (task.completed ? 'checked' : '') + '>' +
                        '<label class="popup-task-text" for="task' + task.id + '">' +
                        task.text +
                        '</label>';
                    
                    const checkbox = taskItem.querySelector('.task-checkbox');
                    checkbox.addEventListener('change', function() {
                        task.completed = this.checked;
                        updateTaskStatus(day);
                    });
                    
                    taskList.appendChild(taskItem);
                });
            }
            
            popup.classList.add('show');
            overlay.classList.add('show');
            updateLeftTaskPanel();
        }

        // 关闭弹出框
        function closePopup() {
            const popup = document.getElementById('taskPopup');
            const overlay = document.getElementById('popupOverlay');
            popup.classList.remove('show');
            overlay.classList.remove('show');
        }

        // 更新左侧任务面板
        function updateLeftTaskPanel() {
            const pendingList = document.getElementById('leftPendingList');
            const completedList = document.getElementById('leftCompletedList');
            
            pendingList.innerHTML = '';
            completedList.innerHTML = '';
            
            const tasks = dailyTasks[currentDay] || [];
            tasks.forEach(task => {
                const taskItem = document.createElement('div');
                taskItem.className = 'task-item';
                taskItem.innerHTML = `
                    <div class="task-content">
                        <span class="task-title">${task.text}</span>
                    </div>
                `;
                
                if (task.completed) {
                    completedList.appendChild(taskItem);
                } else {
                    pendingList.appendChild(taskItem);
                }
            });

            if (pendingList.children.length === 0) {
                pendingList.innerHTML = '<div class="task-item"><div class="task-content"><span class="task-title">暂无未完成任务</span></div></div>';
            }
            if (completedList.children.length === 0) {
                completedList.innerHTML = '<div class="task-item"><div class="task-content"><span class="task-title">暂无已完成任务</span></div></div>';
            }
        }

        // 更新当前天数显示
        function updateDayDisplay(day) {
            const dayDisplay = document.getElementById('currentDayDisplay');
            dayDisplay.textContent = '第' + day + '天';
        }

        // 更新任务状态
        function updateTaskStatus(day) {
            const tasks = dailyTasks[day] || [];
            const allCompleted = tasks.length > 0 && tasks.every(task => task.completed);
            const dot = document.querySelector(`.timeline-dot[data-day="${day}"]`);
            
            if (allCompleted) {
                dot.classList.add('completed');
            } else {
                dot.classList.remove('completed');
            }
            
            updateLeftTaskPanel();
        }

        // 页面加载时初始化
        document.addEventListener('DOMContentLoaded', function() {
            generateTimeline();
            updateLeftTaskPanel();
            
            // 点击遮罩层关闭弹出框
            document.getElementById('popupOverlay').addEventListener('click', closePopup);
        });
    </script>
</body>
</html> 
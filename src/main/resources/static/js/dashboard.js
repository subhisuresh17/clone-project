document.addEventListener('DOMContentLoaded', function() {
    fetch('/dashboard/data')
        .then(response => response.json())
        .then(data => {
            updateDashboard(data);
        })
        .catch(error => {
            console.error('Error fetching data:', error);
        });
 
    function updateDashboard(data) {
        // Update progress value
        const progressElement = document.getElementById('progress-value');
        if (progressElement) {
            progressElement.innerText = `${data.progress}%`;
        }
 
        // Update remaining tasks value
        const remainingTasksElement = document.getElementById('remaining-tasks-value');
        if (remainingTasksElement) {
            remainingTasksElement.innerText = data.remainingTasks;
        }
 
        // Update completed tasks value
        const completedTasksElement = document.getElementById('completed-tasks-value');
        if (completedTasksElement) {
            completedTasksElement.innerText = data.completedTasks;
        }
 
        // Update total tasks value
        const totalTasksElement = document.getElementById('total-tasks-value');
        if (totalTasksElement) {
            totalTasksElement.innerText = data.totalTasks;
        }
 
        // Create pie chart for progress
        const progressChartCanvas = document.createElement('canvas');
        progressChartCanvas.width = 150;
        progressChartCanvas.height = 150;
 
        const progressChartContainer = document.getElementById('progress-chart');
        if (progressChartContainer) {
            progressChartContainer.innerHTML = ''; // Clear previous content
            progressChartContainer.appendChild(progressChartCanvas);
 
            new Chart(progressChartCanvas, {
                type: 'pie',
                data: {
                    labels: ['Completed', 'Remaining'],
                    datasets: [{
                        data: [data.completedTasks, data.remainingTasks],
                        backgroundColor: ['#36a2eb', '#ff6384']
                    }]
                },
                options: {
                    responsive: true
                }
            });
        }
    }
});
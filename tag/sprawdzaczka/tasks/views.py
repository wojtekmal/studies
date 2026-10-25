from django.shortcuts import render, get_object_or_404

from .models import Task

def task_list(request):
    tasks = Task.objects.order_by("short_name")
    return render(request, "tasks/task_list.html", {"tasks": tasks})


def task_detail(request, task_id):
    task = get_object_or_404(Task, pk=task_id)
    return render(request, "tasks/task_detail.html", {"task": task})
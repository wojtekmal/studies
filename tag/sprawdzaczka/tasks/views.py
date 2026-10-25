from django.shortcuts import render, get_object_or_404
from django.contrib.auth.decorators import login_required

from .models import Task

def task_list(request):
    tasks = Task.objects.order_by("short_name")
    return render(request, "tasks/task_list.html", {"tasks": tasks})


def task_detail(request, task_id):
    task = get_object_or_404(Task, pk=task_id)
    return render(request, "tasks/task_detail.html", {"task": task})

@login_required
def submit(request, task_id):
    task = get_object_or_404(Task, pk=task_id)

    if request.method == "POST":
        form = SubmissionForm(request.POST)

        if form.is_valid():
            sub = form.save(commit=False)
            sub.task = task
            sub.user = request.user
            sub.save()
            return redirect("task_detail", task_id=task.id)
    else:
        form = SubmissionForm()
        return render(request, "tasks/submit.html", {"form": form, "task": task})
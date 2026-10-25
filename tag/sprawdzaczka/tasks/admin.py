from django.contrib import admin
from .models import Submission, Task
# Register your models here.

@admin.register(Task)
class TaskAdmin(admin.ModelAdmin):
    list_display = ["short_name", "title", "deadline"]
    search_fields = ["title"]


admin.site.register(Submission)
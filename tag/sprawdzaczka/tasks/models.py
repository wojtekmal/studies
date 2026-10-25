from django.db import models
from django.conf import settings

# Create your models here.

class Task(models.Model):
    short_name = models.CharField(max_length=10, unique=True)
    title = models.CharField(max_length=200)
    statement = models.TextField(blank=True)
    deadline = models.DateTimeField(null=True, blank=True)

    def __str__(self):
        return f"{self.short_name}: {self.title}"

class Submission(models.Model):
    task = models.ForeignKey(Task, on_delete=models.CASCADE, related_name="submissions")
    user = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE)
    code = models.TextField()
    date = models.DateTimeField(auto_now_add=True)
    score = models.IntegerField(null=True, blank=True)
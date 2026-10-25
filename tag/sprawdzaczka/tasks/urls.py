from django.urls import path
from . import views

urlpatterns = [
    path("hello/", views.hello, name="hello"),
    path("", views.task_list, name="task_list"),
    path("<int:task_id>/", views.task_detail, name="task_detail"),
    path("<int:task_id>/submit/", views.submit, name="submit"),
]
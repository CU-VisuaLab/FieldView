from django.conf.urls import url
from rest_framework.urlpatterns import format_suffix_patterns
from . import views

urlpatterns = [
    url(r'^entry/$', views.entry_show),
    url(r'^entry/(?P<pk>[0-9]+)/$', views.entry_detail),
]

urlpatterns = format_suffix_patterns(urlpatterns)
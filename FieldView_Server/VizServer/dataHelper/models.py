from django.db import models


# Create your models here.
class DataEntry(models.Model):
    uid = models.CharField(max_length=100) # author id
    created = models.CharField(max_length=100) # auto_now_add=True, blank=True
    title = models.CharField(max_length=150)
    description = models.TextField()
    longitude = models.CharField(max_length=100)
    latitude = models.CharField(max_length=100)
    altitude = models.CharField(max_length=100)
    objects = models.Manager()

    class Meta:
        ordering = ('created', 'uid')
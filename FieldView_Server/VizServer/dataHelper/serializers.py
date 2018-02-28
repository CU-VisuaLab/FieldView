from rest_framework import serializers
from .models import DataEntry
# from datetime import datetime


class DataEntrySerializer(serializers.ModelSerializer):
    class Meta:
        model = DataEntry
        fields = ('uid', 'title', 'description', 'longitude', 'latitude', 'altitude', 'created')
# class DataEntrySerializer(serializers.Serializer):
#     uid = serializers.CharField(max_length=100)
#     title = serializers.CharField(max_length=100)
#     description = serializers.CharField()
#     longitude = serializers.FloatField(default=0.0)
#     latitude = serializers.FloatField(default=0.0)
#     altitude = serializers.FloatField(default=0.0)
#     created = serializers.DateTimeField(default=datetime.now)
#
#     def create(self, validated_data):
#         """
#
#         :param validated_data:
#         :return:
#         """
#         return DataEntry.objects.create(**validated_data)
#
#     def update(self, instance, validated_data):
#         """
#
#         :param instance:
#         :param validated_data:
#         :return:
#         """
#         instance.uid = validated_data('uid', instance.uid)
#         instance.title = validated_data('title', instance.title)
#         instance.description = validated_data('description', instance.description)
#         instance.longitude = validated_data('longitude', instance.longitude)
#         instance.latitude = validated_data('latitude', instance.latitude)
#         instance.altitude = validated_data('altitude', instance.altitude)
#         instance.created = validated_data('created', instance.created)
#
#         instance.save()
#         return instance

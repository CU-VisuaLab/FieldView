from rest_framework.decorators import api_view
from rest_framework.parsers import JSONParser
from rest_framework import status
from rest_framework.response import Response
from .models import DataEntry
from .serializers import DataEntrySerializer


# Create your views here.
@api_view(['GET', 'POST'])
def entry_show(request, format=None):
    """
    Show all current data or create a new entry
    :param request:
    :return:
    """
    if request.method == 'GET':
        entry = DataEntry.objects.all()
        serializer = DataEntrySerializer(entry, many=True)
        return Response(serializer.data)
    elif request.method == 'POST':
        print(request.body)
        #print(request.data)
        data = JSONParser().parse(request)
        serializer = DataEntrySerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        print(data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


@api_view(['GET', 'PUT', 'DELETE'])
def entry_detail(request, pk, format=None):
    """
    Retrieve, update or delete a data entry.
    """
    try:
        entry = DataEntry.objects.get(pk=pk)
    except DataEntry.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    if request.method == 'GET':
        serializer = DataEntrySerializer(entry)
        return Response(serializer.data)

    elif request.method == 'PUT':
        data = JSONParser().parse(request)
        serializer = DataEntrySerializer(entry, data=data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    elif request.method == 'DELETE':
        entry.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
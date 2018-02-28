# Prerequisite
1. sudo apt-get install mysql-server libmysqlclient-dev
2. sudo apt-get install httpie


# Test Insert data
http POST http://127.0.0.1:8000/entry/ uid='keke' created='2018-02-11 19:31:18.176562' title='test' description='Today we had a very heavy snow' longitude=21321 latitude=321321 altitude=21321



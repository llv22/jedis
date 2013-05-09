--  reference, see http://www.redisgreen.net/blog/2013/03/18/intro-to-lua-for-redis-programmers/
local conkey = 'PHASE1CONSUMER'
local consecret = 'PHASE1SECRET'
local access_tokens = KEYS[1]
local token_secret = KEYS[2]
local time = KEYS[3]
local key = string.format('access_tokens:%s', access_tokens)
redis.log(redis.LOG_VERBOSE, string.format('%s', key))
local ex = redis.call('EXISTS', key)
redis.log(redis.LOG_VERBOSE, string.format('Existing testing return value - %d', ex))
if ex == 1 then
	local ttl = redis.call('TTL', key)
	return {'exists', 'true', 'ttl', string.format('%d', ttl)}
else
	redis.call('HMSET', key, 'consumer_key', conkey, 'consumer_secret', consecret, 'token_secret', token_secret, 'timestamp', time)
	-- testing purpose
	redis.call('EXPIRE', key, '1000')
	-- redis.call('EXPIRE', rediskey, '43200')
	return {'access_tokens', access_tokens, 'token_secret', token_secret, 'key', key, 'exists', 'false'}
end
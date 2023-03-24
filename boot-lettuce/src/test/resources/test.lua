local isExists = redis.call('exists',KEYS[1])
if isExists == 0 then
   redis.call('set',KEYS[1],'0')
end
local isExists2 = redis.call('exists',KEYS[2])
if isExists2 == 0 then
   redis.call('set',KEYS[2],'0')
end

local money1 = redis.call('get',KEYS[1])
if isExists == 1 and hsyMoney > KEYS[1] then
    redis.call('decrby',KEYS[1],ARGV[1])
    redis.call('incrby',KEYS[2],ARGV[1])
end

local moneyMap = {}
moneyMap['1'] = money1
moneyMap['2'] = money2

return cjson.encode(fileNoMap)
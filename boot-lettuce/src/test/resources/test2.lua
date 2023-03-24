local money1 = redis.call('hget','money','hsy')
if isExists == 1 and money1 > ARGV[1] then
    redis.call('hdecrby','money','hsy', ARGV[1])
    redis.call('hincrby','money','zyt', ARGV[1])
end

local money2 = redis.call('hget','money','zyt')
local moneyMap = {}
moneyMap['1'] = money1
moneyMap['2'] = money2


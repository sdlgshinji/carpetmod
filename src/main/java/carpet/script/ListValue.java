package carpet.script;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class ListValue extends Value
{
    protected List<Value> items;
    @Override
    public String getString()
    {
        return "["+items.stream().map(Value::getString).collect(Collectors.joining(", "))+"]";
    }

    @Override
    public boolean getBoolean() {
        return !items.isEmpty();
    }

    @Override
    public Value clone()
    {
        return new ListValue(items);
    }
    public ListValue(Collection<? extends Value> list)
    {
        items = new ArrayList<>();
        items.addAll(list);
    }
    public static ListValue wrap(List<Value> list)
    {
        ListValue created = new ListValue();
        created.items = list;
        return created;
    }
    public static ListValue of(Value ... list)
    {
        return ListValue.wrap(Arrays.asList(list));
    }

    private ListValue()
    {
        items = new ArrayList<>();
    }

    @Override
    public Value add(Value other) {
        ListValue output = new ListValue();
        if (other instanceof ListValue)
        {
            List<Value> other_list = ((ListValue) other).items;
            if (other_list.size() == items.size())
            {
                for(int i = 0, size = items.size(); i < size; i++)
                {
                    output.items.add(items.get(i).add(other_list.get(i)));
                }
            }
            else
            {
                throw new Expression.InternalExpressionException("Cannot subtract two lists of uneven sizes");
            }
        }
        else
        {
            for (Value v : items)
            {
                output.items.add(v.add(other));
            }
        }
        return output;
    }
    public void append(Value v)
    {
        items.add(v);
    }
    public Value subtract(Value other)
    {
        ListValue output = new ListValue();
        if (other instanceof ListValue)
        {
            List<Value> other_list = ((ListValue) other).items;
            if (other_list.size() == items.size())
            {
                for(int i = 0, size = items.size(); i < size; i++)
                {
                    output.items.add(items.get(i).subtract(other_list.get(i)));
                }
            }
            else
            {
                throw new Expression.InternalExpressionException("Cannot subtract two lists of uneven sizes");
            }
        }
        else
        {
            for (Value v : items)
            {
                output.items.add(v.subtract(other));
            }
        }
        return output;
    }
    public void subtractFrom(Value v) // if I ever do -= then it wouod remove items
    {
        throw new UnsupportedOperationException(); // TODO
    }


    public Value multiply(Value other)
    {
        ListValue output = new ListValue();
        if (other instanceof ListValue)
        {
            List<Value> other_list = ((ListValue) other).items;
            if (other_list.size() == items.size())
            {
                for(int i = 0, size = items.size(); i < size; i++)
                {
                    output.items.add(items.get(i).multiply(other_list.get(i)));
                }
            }
            else
            {
                throw new Expression.InternalExpressionException("Cannot subtract two lists of uneven sizes");
            }
        }
        else
        {
            for (Value v : items)
            {
                output.items.add(v.multiply(other));
            }
        }
        return output;
    }
    public Value divide(Value other)
    {
        ListValue output = new ListValue();
        if (other instanceof ListValue)
        {
            List<Value> other_list = ((ListValue) other).items;
            if (other_list.size() == items.size())
            {
                for(int i = 0, size = items.size(); i < size; i++)
                {
                    output.items.add(items.get(i).divide(other_list.get(i)));
                }
            }
            else
            {
                throw new Expression.InternalExpressionException("Cannot subtract two lists of uneven sizes");
            }
        }
        else
        {
            for (Value v : items)
            {
                output.items.add(v.divide(other));
            }
        }
        return output;
    }

    @Override
    public int compareTo(Value o)
    {
        if (o instanceof ListValue)
        {
            ListValue ol = (ListValue)o;
            int this_size = this.getItems().size();
            int o_size = ol.getItems().size();
            if (this_size != o_size)
            {
                return this_size - o_size;
            }
            if (this_size == 0)
            {
                return 0;
            }
            for (int i = 0; i < this_size; i++)
            {
                int res = this.items.get(i).compareTo(ol.items.get(i));
                if (res != 0)
                {
                    return res;
                }
            }
            return 0;
        }
        return super.compareTo(o);
    }

    public List<Value> getItems()
    {
        return items;
    }

    public Iterator<Value> iterator()
    {
        return items.iterator();
    }
    public void fatality()
    {
    }


    public static class ListConstructorValue extends ListValue
    {
        public ListConstructorValue(Collection<? extends Value> list)
        {
            super(list);
        }
    }
    public int length()
    {
        return items.size();
    }

    @Override
    public Value in(Value value1)
    {
        for (int i = 0; i < items.size(); i++)
        {
            Value v = items.get(i);
            if (v.equals(value1))
            {
                return new NumericValue(i);
            }
        }
        return Value.NULL;
    }

    @Override
    public Value slice(long from, long to)
    {
        List<Value> items = getItems();
        int size = items.size();
        if (to < 0 || to > size) to = size;
        if (from < 0 || from > size) from = size;
        if (from > to)
            return ListValue.of();
        return new ListValue(getItems().subList((int)from, (int) to));
    }

    @Override
    public double readNumber()
    {
        return (double)items.size();
    }



}

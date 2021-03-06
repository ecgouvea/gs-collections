/*
 * Copyright 2015 Goldman Sachs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gs.collections.impl.list.fixed;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

import com.gs.collections.api.block.HashingStrategy;
import com.gs.collections.api.block.function.Function;
import com.gs.collections.api.block.function.Function0;
import com.gs.collections.api.block.function.Function2;
import com.gs.collections.api.block.function.Function3;
import com.gs.collections.api.block.predicate.Predicate;
import com.gs.collections.api.block.predicate.Predicate2;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.api.block.procedure.primitive.ObjectIntProcedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.ordered.OrderedIterable;
import com.gs.collections.api.tuple.Twin;
import com.gs.collections.impl.block.factory.Predicates2;
import com.gs.collections.impl.list.mutable.AbstractMutableList;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.list.mutable.ListAdapter;
import com.gs.collections.impl.utility.ArrayIterate;
import com.gs.collections.impl.utility.Iterate;
import com.gs.collections.impl.utility.ListIterate;
import com.gs.collections.impl.utility.internal.InternalArrayIterate;
import com.gs.collections.impl.utility.internal.RandomAccessListIterate;

public abstract class AbstractArrayAdapter<T>
        extends AbstractMutableList<T>
        implements RandomAccess
{
    private static final Object[] OBJECTS = {};
    protected T[] items;

    /**
     * This method must be here so subclasses can be serializable.
     */
    protected AbstractArrayAdapter()
    {
        this.items = (T[]) OBJECTS;
    }

    protected AbstractArrayAdapter(T[] newElements)
    {
        if (newElements == null)
        {
            throw new IllegalArgumentException("items cannot be null");
        }
        this.items = newElements;
    }

    @Override
    public boolean notEmpty()
    {
        return this.items.length > 0;
    }

    @Override
    public T getFirst()
    {
        return this.isEmpty() ? null : this.items[0];
    }

    @Override
    public T getLast()
    {
        return this.isEmpty() ? null : this.items[this.items.length - 1];
    }

    @Override
    public void each(Procedure<? super T> procedure)
    {
        int size = this.size();
        for (int i = 0; i < size; i++)
        {
            procedure.value(this.items[i]);
        }
    }

    @Override
    public void forEachWithIndex(ObjectIntProcedure<? super T> objectIntProcedure)
    {
        InternalArrayIterate.forEachWithIndex(this.items, this.items.length, objectIntProcedure);
    }

    @Override
    public void forEachWithIndex(int fromIndex, int toIndex, ObjectIntProcedure<? super T> objectIntProcedure)
    {
        ListIterate.rangeCheck(fromIndex, toIndex, this.items.length);
        InternalArrayIterate.forEachWithIndexWithoutChecks(this.items, fromIndex, toIndex, objectIntProcedure);
    }

    @Override
    public boolean removeIf(Predicate<? super T> predicate)
    {
        throw new UnsupportedOperationException("Cannot call removeIf() on " + this.getClass().getSimpleName());
    }

    @Override
    public <P> boolean removeIfWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        throw new UnsupportedOperationException("Cannot call removeIfWith() on " + this.getClass().getSimpleName());
    }

    @Override
    public T detect(Predicate<? super T> predicate)
    {
        return InternalArrayIterate.detect(this.items, this.items.length, predicate);
    }

    @Override
    public T detectIfNone(Predicate<? super T> predicate, Function0<? extends T> function)
    {
        T result = this.detect(predicate);
        return result == null ? function.value() : result;
    }

    @Override
    public int detectIndex(Predicate<? super T> predicate)
    {
        return InternalArrayIterate.detectIndex(this.items, this.items.length, predicate);
    }

    @Override
    public int detectLastIndex(Predicate<? super T> predicate)
    {
        return InternalArrayIterate.detectLastIndex(this.items, this.items.length, predicate);
    }

    @Override
    public int count(Predicate<? super T> predicate)
    {
        return InternalArrayIterate.count(this.items, this.items.length, predicate);
    }

    @Override
    public <S> boolean corresponds(OrderedIterable<S> other, Predicate2<? super T, ? super S> predicate)
    {
        return InternalArrayIterate.corresponds(this.items, this.items.length, other, predicate);
    }

    @Override
    public boolean anySatisfy(Predicate<? super T> predicate)
    {
        return InternalArrayIterate.anySatisfy(this.items, this.items.length, predicate);
    }

    @Override
    public boolean allSatisfy(Predicate<? super T> predicate)
    {
        return InternalArrayIterate.allSatisfy(this.items, this.items.length, predicate);
    }

    @Override
    public boolean noneSatisfy(Predicate<? super T> predicate)
    {
        return InternalArrayIterate.noneSatisfy(this.items, this.items.length, predicate);
    }

    @Override
    public <IV> IV injectInto(IV injectedValue, Function2<? super IV, ? super T, ? extends IV> function)
    {
        return ArrayIterate.injectInto(injectedValue, this.items, function);
    }

    @Override
    public MutableList<T> select(Predicate<? super T> predicate)
    {
        return this.select(predicate, FastList.<T>newList());
    }

    @Override
    public <R extends Collection<T>> R select(Predicate<? super T> predicate, R target)
    {
        return InternalArrayIterate.select(this.items, this.items.length, predicate, target);
    }

    @Override
    public MutableList<T> reject(Predicate<? super T> predicate)
    {
        return this.reject(predicate, FastList.<T>newList());
    }

    @Override
    public <R extends Collection<T>> R reject(Predicate<? super T> predicate, R target)
    {
        return InternalArrayIterate.reject(this.items, this.items.length, predicate, target);
    }

    @Override
    public <V> MutableList<V> collect(Function<? super T, ? extends V> function)
    {
        return this.collect(function, FastList.<V>newList(this.size()));
    }

    @Override
    public <V, R extends Collection<V>> R collect(Function<? super T, ? extends V> function, R target)
    {
        return InternalArrayIterate.collect(this.items, this.items.length, function, target);
    }

    @Override
    public <V> MutableList<V> collectIf(
            Predicate<? super T> predicate,
            Function<? super T, ? extends V> function)
    {
        return this.collectIf(predicate, function, FastList.<V>newList());
    }

    @Override
    public <V, R extends Collection<V>> R collectIf(
            Predicate<? super T> predicate, Function<? super T, ? extends V> function, R target)
    {
        return InternalArrayIterate.collectIf(this.items, this.items.length, predicate, function, target);
    }

    @Override
    public <V> MutableList<V> flatCollect(Function<? super T, ? extends Iterable<V>> function)
    {
        return InternalArrayIterate.flatCollect(this.items, this.items.length, function, FastList.<V>newList(this.items.length));
    }

    @Override
    public <V, R extends Collection<V>> R flatCollect(Function<? super T, ? extends Iterable<V>> function, R target)
    {
        return InternalArrayIterate.flatCollect(this.items, this.items.length, function, target);
    }

    @Override
    public <P> Twin<MutableList<T>> selectAndRejectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter)
    {
        return InternalArrayIterate.selectAndRejectWith(this.items, this.items.length, predicate, parameter);
    }

    public int size()
    {
        return this.items.length;
    }

    @Override
    public boolean isEmpty()
    {
        return this.items.length == 0;
    }

    @Override
    public boolean contains(Object o)
    {
        return InternalArrayIterate.anySatisfyWith(this.items, this.items.length, Predicates2.equal(), o);
    }

    @Override
    public Iterator<T> iterator()
    {
        return Arrays.asList(this.items).iterator();
    }

    @Override
    public Object[] toArray()
    {
        return this.items.clone();
    }

    @Override
    public <E> E[] toArray(E[] array)
    {
        int size = this.size();
        E[] result = array.length < size
                ? (E[]) Array.newInstance(array.getClass().getComponentType(), size)
                : array;

        System.arraycopy(this.items, 0, result, 0, size);
        if (result.length > size)
        {
            result[size] = null;
        }
        return result;
    }

    @Override
    public boolean remove(Object o)
    {
        throw new UnsupportedOperationException("Cannot call remove() on " + this.getClass().getSimpleName());
    }

    @Override
    public boolean containsAll(Collection<?> collection)
    {
        return Iterate.allSatisfyWith(collection, Predicates2.in(), this);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection)
    {
        throw new UnsupportedOperationException("Cannot call addAll() on " + this.getClass().getSimpleName());
    }

    @Override
    public boolean addAllIterable(Iterable<? extends T> iterable)
    {
        throw new UnsupportedOperationException("Cannot call addAllIterable() on " + this.getClass().getSimpleName());
    }

    @Override
    public boolean removeAll(Collection<?> collection)
    {
        throw new UnsupportedOperationException("Cannot call removeAll() on " + this.getClass().getSimpleName());
    }

    @Override
    public boolean removeAllIterable(Iterable<?> iterable)
    {
        throw new UnsupportedOperationException("Cannot call removeAllIterable() on " + this.getClass().getSimpleName());
    }

    @Override
    public boolean retainAll(Collection<?> collection)
    {
        throw new UnsupportedOperationException("Cannot call retainAll() on " + this.getClass().getSimpleName());
    }

    @Override
    public boolean retainAllIterable(Iterable<?> iterable)
    {
        throw new UnsupportedOperationException("Cannot call retainAllIterable() on " + this.getClass().getSimpleName());
    }

    public void clear()
    {
        throw new UnsupportedOperationException("Cannot call clear() on " + this.getClass().getSimpleName());
    }

    public boolean addAll(int index, Collection<? extends T> collection)
    {
        throw new UnsupportedOperationException("Cannot call addAll() on " + this.getClass().getSimpleName());
    }

    public T get(int index)
    {
        if (index >= this.size())
        {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size());
        }

        return this.items[index];
    }

    public void add(int index, T element)
    {
        throw new UnsupportedOperationException("Cannot call add() on " + this.getClass().getSimpleName());
    }

    public T remove(int index)
    {
        throw new UnsupportedOperationException("Cannot call remove() on " + this.getClass().getSimpleName());
    }

    @Override
    public int indexOf(Object item)
    {
        return InternalArrayIterate.indexOf(this.items, this.items.length, item);
    }

    @Override
    public int lastIndexOf(Object item)
    {
        return InternalArrayIterate.lastIndexOf(this.items, this.items.length, item);
    }

    @Override
    public ListIterator<T> listIterator(int index)
    {
        return Arrays.asList(this.items).listIterator(index);
    }

    @Override
    public MutableList<T> subList(int fromIndex, int toIndex)
    {
        return ListAdapter.adapt(Arrays.asList(this.items).subList(fromIndex, toIndex));
    }

    @Override
    public boolean equals(Object that)
    {
        if (that == this)
        {
            return true;
        }
        if (!(that instanceof List))
        {
            return false;
        }
        if (that instanceof AbstractArrayAdapter)
        {
            return this.abstractArrayAdapterEquals((AbstractArrayAdapter<?>) that);
        }
        return InternalArrayIterate.arrayEqualsList(this.items, this.items.length, (List<?>) that);
    }

    public boolean abstractArrayAdapterEquals(AbstractArrayAdapter<?> list)
    {
        return Arrays.equals(this.items, list.items);
    }

    @Override
    public int hashCode()
    {
        return Arrays.hashCode(this.items);
    }

    @Override
    public <P> void forEachWith(Procedure2<? super T, ? super P> procedure, P parameter)
    {
        for (T each : this.items)
        {
            procedure.value(each, parameter);
        }
    }

    @Override
    public <P> MutableList<T> selectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.selectWith(predicate, parameter, FastList.<T>newList());
    }

    @Override
    public <P, R extends Collection<T>> R selectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R target)
    {
        return InternalArrayIterate.selectWith(this.items, this.items.length, predicate, parameter, target);
    }

    @Override
    public <P> MutableList<T> rejectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return this.rejectWith(predicate, parameter, FastList.<T>newList());
    }

    @Override
    public <P, R extends Collection<T>> R rejectWith(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            R target)
    {
        return InternalArrayIterate.rejectWith(this.items, this.items.length, predicate, parameter, target);
    }

    @Override
    public <P, A> MutableList<A> collectWith(Function2<? super T, ? super P, ? extends A> function, P parameter)
    {
        return this.collectWith(function, parameter, FastList.<A>newList());
    }

    @Override
    public <P, A, R extends Collection<A>> R collectWith(
            Function2<? super T, ? super P, ? extends A> function,
            P parameter,
            R target)
    {
        return InternalArrayIterate.collectWith(this.items, this.items.length, function, parameter, target);
    }

    @Override
    public <IV, P> IV injectIntoWith(
            IV injectValue, Function3<? super IV, ? super T, ? super P, ? extends IV> function, P parameter)
    {
        return ArrayIterate.injectIntoWith(injectValue, this.items, function, parameter);
    }

    @Override
    public void forEach(int fromIndex, int toIndex, Procedure<? super T> procedure)
    {
        ListIterate.rangeCheck(fromIndex, toIndex, this.items.length);
        InternalArrayIterate.forEachWithoutChecks(this.items, fromIndex, toIndex, procedure);
    }

    @Override
    public <P> T detectWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return InternalArrayIterate.detectWith(this.items, this.items.length, predicate, parameter);
    }

    @Override
    public <P> T detectWithIfNone(
            Predicate2<? super T, ? super P> predicate,
            P parameter,
            Function0<? extends T> function)
    {
        T result = this.detectWith(predicate, parameter);
        return result == null ? function.value() : result;
    }

    @Override
    public <P> int countWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return InternalArrayIterate.countWith(this.items, this.items.length, predicate, parameter);
    }

    @Override
    public <P> boolean anySatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return InternalArrayIterate.anySatisfyWith(this.items, this.items.length, predicate, parameter);
    }

    @Override
    public <P> boolean allSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return InternalArrayIterate.allSatisfyWith(this.items, this.items.length, predicate, parameter);
    }

    @Override
    public <P> boolean noneSatisfyWith(Predicate2<? super T, ? super P> predicate, P parameter)
    {
        return InternalArrayIterate.noneSatisfyWith(this.items, this.items.length, predicate, parameter);
    }

    @Override
    public MutableList<T> distinct()
    {
        return InternalArrayIterate.distinct(this.items, this.items.length);
    }

    @Override
    public MutableList<T> distinct(HashingStrategy<? super T> hashingStrategy)
    {
        return InternalArrayIterate.distinct(this.items, this.items.length, hashingStrategy);
    }

    @Override
    public void appendString(Appendable appendable, String start, String separator, String end)
    {
        InternalArrayIterate.appendString(this, this.items, this.items.length, appendable, start, separator, end);
    }

    @Override
    public MutableList<T> take(int count)
    {
        return RandomAccessListIterate.take(this, count);
    }

    @Override
    public MutableList<T> drop(int count)
    {
        return RandomAccessListIterate.drop(this, count);
    }
}

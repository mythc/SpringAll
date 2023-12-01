package cc.mrbird.batch.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

/**
 * @author fanglihua
 * @version 1.0
 * @date 2023/11/27 14:23
 */
@Component
public class MySimpleItemReader implements ItemReader {

    private Iterator<String> iterator;

    public MySimpleItemReader(List<String> data) {
        this.iterator = data.iterator();
    }
    @Override
    public Object read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return iterator.hasNext()? iterator.next() : null;
    }
}

package cloud.longfa.encrypt.spel;

import cloud.longfa.encrypt.handler.ScenarioSchedule;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.NonNull;

/**
 * The type Sp el expression handler.
 *
 * @author : longfa
 * @email : longfa0130@gmail.com
 * @description : 解析SpEL表达式
 * @since : 1.0.0
 */
public class SpELExpressionHandler implements BeanFactoryAware, InitializingBean {
    private SpelExpressionParser spelExpressionParser;
    private BeanFactory beanFactory;

    /**
     * Sets spel expression parser.
     *
     * @param spelExpressionParser the spel expression parser
     */
    public void setSpelExpressionParser(SpelExpressionParser spelExpressionParser) {
        this.spelExpressionParser = spelExpressionParser;
    }

    @Override
    public void afterPropertiesSet() {
        ScenarioSchedule.spELExpressionHandler = this;
    }

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    /**
     * Parse sp el expression string [ ].
     *
     * @param expression the expression
     * @return the string [ ]
     */
//解析表达式
    public String[] parseSpELExpression(String expression){
        //上下文 保存的是外部信息 提供给解析器解析
        StandardEvaluationContext standardContext = new StandardEvaluationContext();
        //解析beanFactory 存储bean的实例
        standardContext.setBeanResolver(new BeanFactoryResolver(beanFactory));
        //解析实例 自定义模板 ::内容区::
        Expression parseExpression = this.spelExpressionParser.parseExpression(expression, SpELParserContext.PARSER_CONTEXT);
        //解析出数组
        String[] value;
        try {
            value = parseExpression.getValue(standardContext, String[].class);
        }catch (SpelEvaluationException spelEvaluationException){
            throw new RuntimeException("无法解析你的所写的表达式 请检查");
        }
        return value;
    }


}

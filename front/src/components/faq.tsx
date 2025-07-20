import { useState } from 'react';

interface FAQItem {
  question: string;
  answer: string;
}

const faqData: FAQItem[] = [
  {
    question: "Do I need to pay to use TeamSync?",
    answer: "Mostly no. We don't charge any fees for using our platform to find a team. However, if you want to integrate TeamSync into your campus or hire someone through the platform, please contact us for more information."
  },
  {
    question: "Can I join multiple teams at the same time?",
    answer: "Yes, you can participate in several projects simultaneously, as long as you can manage your commitments."
  },
  {
    question: "How does the matching algorithm work?",
    answer: "Our AI-powered system analyzes your skills, interests, and project preferences to suggest the best team matches for you."
  },
  {
    question: "What if I don't find a suitable team?",
    answer: "If you don't find a suitable team right away, you can start your own project and invite others to join. New teams and members appear regularly, so keep checking back for new opportunities."
  },
  {
    question: "How can I contact support?",
    answer: "You can reach our support team via Telegram for quick assistance. Just message us at @TeamSync"
  }
];

interface FAQItemProps {
  item: FAQItem;
  isOpen: boolean;
  onToggle: () => void;
}

function FAQItemComponent({ item, isOpen, onToggle }: FAQItemProps) {
  return (
    <div className="border-b border-gray-300 last:border-b-0">
      <button
        className="w-full py-8 flex items-center justify-between text-left hover:opacity-75 transition-opacity duration-200 cursor-pointer"
        onClick={onToggle}
      >
        <h3 className="font-[Inter] text-3xl font-semibold text-(--secondary-color) pr-8">
          {item.question}
        </h3>
        <img
          src="./chevron.svg"
          alt="Expand"
          className={`w-8 h-8 flex-shrink-0 transition-transform duration-300 ease-in-out ${
            isOpen ? 'rotate-180' : 'rotate-0'
          }`}
        />
      </button>
      <div
        className={`overflow-hidden transition-all duration-300 ease-in-out ${
          isOpen ? 'max-h-96 opacity-100 pb-8' : 'max-h-0 opacity-0'
        }`}
      >
        <div className="pr-16">
          <p className="font-[Inter] text-xl text-(--secondary-color) leading-relaxed">
            {item.answer}
          </p>
        </div>
      </div>
    </div>
  );
}

export default function FAQ() {
  const [openItems, setOpenItems] = useState<Set<number>>(new Set());

  const toggleItem = (index: number) => {
    const newOpenItems = new Set(openItems);
    if (newOpenItems.has(index)) {
      newOpenItems.delete(index);
    } else {
      newOpenItems.add(index);
    }
    setOpenItems(newOpenItems);
  };

  return (
    <div className="text-(--secondary-color) flex flex-col items-center py-32">
      <h1 className="font-[Manrope] font-extrabold text-7xl pb-16">FAQ</h1>
      <div className="w-full max-w-6xl mx-auto px-8">
        {faqData.map((item, index) => (
          <FAQItemComponent
            key={index}
            item={item}
            isOpen={openItems.has(index)}
            onToggle={() => toggleItem(index)}
          />
        ))}
      </div>
    </div>
  );
}
